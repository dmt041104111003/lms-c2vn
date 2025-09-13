package com.cardano_lms.server.Controller;

import com.cardano_lms.server.DTO.Request.*;
import com.cardano_lms.server.DTO.Response.*;
import com.cardano_lms.server.Service.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shaded.org.bouncycastle.util.test.Test;

import java.util.List;

@RestController
@RequestMapping("/course")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ApiResponse<CourseCreationResponse> createCourse(@RequestBody CourseCreationRequest courseCreationRequest) {
        return ApiResponse.<CourseCreationResponse>builder()
                .result(courseService.createCourse(courseCreationRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CourseCreationResponse>> getAll() {
        return ApiResponse.<List<CourseCreationResponse>>builder()
                .result(courseService.getCourses())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseCreationResponse> getCourseById(@PathVariable String id) {
        return ApiResponse.<CourseCreationResponse>builder()
                .result(courseService.getCourseById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseUpdateResponse> updateCourse(
            @PathVariable String id,
            @RequestBody CourseUpdateRequest request
    ) {
        return ApiResponse.<CourseUpdateResponse>builder()
                .result(courseService.updateCourse(id, request))
                .message("Course update successfully ")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ApiResponse.<Void>builder()
                .message("Course deleted successfully")
                .build();
    }

    @PostMapping("/{courseId}/chapters")
    public ApiResponse<ChapterResponse> addChapter(
            @PathVariable String courseId,
            @RequestBody ChapterRequest request) {

        ChapterResponse chapterResponse= courseService.addChapterToCourse(courseId, request);
        return ApiResponse.<ChapterResponse>builder()
                .message("Chapter created successfully")
                .result(chapterResponse)
                .build();
    }

    @PostMapping("/chapterId={chapterId}/lectures")
    public ApiResponse<LectureResponse> addLectureToChapter(
            @PathVariable Long chapterId,
            @RequestBody LectureRequest request) {

        LectureResponse lectureResponse= courseService.addLectureToChapter(chapterId, request);
        return ApiResponse.<LectureResponse>builder()
                .message("Lecture created successfully")
                .result(lectureResponse)
                .build();
    }

    @PostMapping("/chapterId={chapterId}/tests")
    public ApiResponse<TestResponse> addLectureToChapter(
            @PathVariable Long chapterId,
            @RequestBody TestRequest request) {

        TestResponse testResponse= courseService.addTest(request, chapterId,null);
        return ApiResponse.<TestResponse>builder()
                .message("Lecture created successfully")
                .result(testResponse)
                .build();
    }
    @PostMapping("/courseId={courseId}/tests")
    public ApiResponse<TestResponse> addLectureToChapter(
            @PathVariable String courseId,
            @RequestBody TestRequest request) {

        TestResponse testResponse = courseService.addTest(request, null,courseId);
        return ApiResponse.<TestResponse>builder()
                .message("Test created successfully")
                .result(testResponse)
                .build();
    }


}

