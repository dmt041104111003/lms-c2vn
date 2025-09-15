package com.cardano_lms.server.Service;

import com.cardano_lms.server.DTO.Request.*;
import com.cardano_lms.server.DTO.Response.*;
import com.cardano_lms.server.Entity.*;
import com.cardano_lms.server.Exception.AppException;
import com.cardano_lms.server.Exception.ErrorCode;
import com.cardano_lms.server.Mapper.*;
import com.cardano_lms.server.Repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {
    CourseRepository courseRepository;
    InstructorProfileRepository instructorProfileRepository;
    PaymentMethodRepository paymentMethodRepository;
    ChapterRepository chapterRepository;
    LectureRepository lectureRepository;
    TestRepository testRepository;
    CourseMapper courseMapper;
    TestMapper testMapper;
    ChapterMapper chapterMapper;
    QuestionMapper questionMapper;
    AnswerMapper answerMapper;
    LectureMapper lectureMapper;

    public CourseCreationResponse createCourse(CourseCreationRequest courseCreationRequest) {

        InstructorProfile instructor = instructorProfileRepository.findById(courseCreationRequest.getInstructorId())
                .orElseThrow(() -> new AppException(ErrorCode.YOU_ARE_NOT_INSTRUCTOR));


        Course course = courseMapper.toCourse(courseCreationRequest);
        course.setInstructor(instructor);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());


        if (courseCreationRequest.getPaymentMethods() != null && !courseCreationRequest.getPaymentMethods().isEmpty()) {
            for (PaymentOptionRequest option : courseCreationRequest.getPaymentMethods()) {
                PaymentMethod method = paymentMethodRepository.findByName(option.getPaymentMethodId())
                        .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));

                CoursePaymentMethod cpm = CoursePaymentMethod.builder()
                        .course(course)
                        .paymentMethod(method)
                        .receiverAddress(option.getReceiverAddress())
                        .build();

                course.getCoursePaymentMethods().add(cpm);
            }
        }

        if (courseCreationRequest.getCourseTests() != null) {
            courseCreationRequest.getCourseTests().forEach(testReq -> {
                buildTestWithQuestions(testReq, course, null);
            });
        }

        if (courseCreationRequest.getChapters() != null) {
            courseCreationRequest.getChapters().forEach(chReq -> {
                Chapter chapter = chapterMapper.toEntity(chReq);
                course.addChapter(chapter);

                if (chReq.getLectures() != null) {
                    chReq.getLectures().forEach(lecReq -> {
                        Lecture lecture = lectureMapper.toEntity(lecReq);
                        chapter.addLecture(lecture);
                    });
                }

                if (chReq.getTests() != null) {
                    chReq.getTests().forEach(testReq -> {
                      buildTestWithQuestions(testReq, null, chapter);
                    });
                }
            });
        }

        Course saved = courseRepository.save(course);
        return courseMapper.toResponse(saved);
    }


    private Test buildTestWithQuestions(TestRequest testReq,
                                        Course course,
                                        Chapter chapter) {
        Test test = testMapper.toEntity(testReq);

        if (course != null) {
            course.addTest(test);
        }
        if (chapter != null) {
            chapter.addTest(test);
        }

        if (testReq.getQuestions() != null) {
            testReq.getQuestions().forEach(qReq -> {
                Question question = questionMapper.toQuestion(qReq);
                test.addQuestion(question);

                if (qReq.getAnswers() != null) {
                    qReq.getAnswers().forEach(aReq -> {
                        Answer answer = answerMapper.toAnswer(aReq);
                        question.addAnswer(answer);
                    });
                }
            });
        }

        return test;
    }


    public List<CourseCreationResponse> getCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public CourseCreationResponse getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseMapper.toResponse(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public CourseUpdateResponse updateCourse(String id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        courseMapper.updateCourseFromRequest(request, course);
        course.setUpdatedAt(LocalDateTime.now());

        Course saved = courseRepository.save(course);
        return courseMapper.toCourseUpdateResponse(saved);
    }



    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        courseRepository.deleteById(id);
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public ChapterResponse addChapterToCourse(String courseId, ChapterRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Chapter newChapter = chapterMapper.toEntity(request);
        newChapter.setCourse(course);
        Chapter savedChapter = chapterRepository.save(newChapter);

        Optional.ofNullable(request.getLectures())
                .ifPresent(list -> list.forEach(lecReq -> {
                    Lecture lecture = lectureMapper.toEntity(lecReq);
                    newChapter.addLecture(lecture);
                }));

        Optional.ofNullable(request.getTests())
                .ifPresent(list -> list.forEach(testReq ->
                        buildTestWithQuestions(testReq, null, newChapter)));

        course.addChapter(newChapter);
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);

        return chapterMapper.toResponse(savedChapter);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public LectureResponse addLectureToChapter(Long chapterId, LectureRequest request) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));

        Lecture newLecture = lectureMapper.toEntity(request);
        newLecture.setChapter(chapter);

        Lecture savedLecture = lectureRepository.save(newLecture);

        chapter.addLecture(newLecture);
        if (chapter.getCourse() != null) {
            chapter.getCourse().setUpdatedAt(LocalDateTime.now());
            courseRepository.save(chapter.getCourse());
        }

        return lectureMapper.toResponse(savedLecture);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public TestResponse addTest(TestRequest request, Long chapterId, String courseId) {

        if (chapterId == null && courseId == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        Test newTest = null;
        Course course = null;
        if (chapterId != null) {
            Chapter chapter = chapterRepository.findById(chapterId)
                    .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));
            newTest = buildTestWithQuestions(request, null, chapter);
        }
        if (courseId != null && chapterId == null) {
            course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
            newTest = buildTestWithQuestions(request, course, null);
        }
        Test saved = testRepository.save(newTest);
        if (course != null) {
            course.setUpdatedAt(LocalDateTime.now());
            courseRepository.save(course);
        }
        return testMapper.toResponse(saved);
    }














}
