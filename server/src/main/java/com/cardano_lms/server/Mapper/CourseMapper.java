package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.CourseCreationRequest;
import com.cardano_lms.server.DTO.Request.CourseUpdateRequest;
import com.cardano_lms.server.DTO.Response.CourseCreationResponse;
import com.cardano_lms.server.DTO.Response.CourseUpdateResponse;
import com.cardano_lms.server.Entity.Course;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { ChapterMapper.class, TestMapper.class
        ,InstructorProfileMapper.class}
)
public interface CourseMapper {
    @Mapping(target = "chapters", ignore = true)
    @Mapping(target = "courseTests", ignore = true)
    @Mapping(target = "coursePaymentMethods", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "price", source = "price")
    @Mapping(target = "currency", source = "currency")
    Course toCourse(CourseCreationRequest request);

    @Mapping(source = "instructor", target = "instructorProfileResponse")
    @Mapping(source = "courseTests", target = "courseTests")
    CourseCreationResponse toResponse(Course course);



    @Mapping(target = "coursePaymentMethods", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "courseTests", ignore = true)
    @Mapping(target = "chapters", ignore = true)
    @Mapping(target = "price", source = "price")
    @Mapping(target = "draft", source = "draft")
    @Mapping(target = "currency", source = "currency")
    void updateCourseFromRequest(CourseUpdateRequest request, @MappingTarget Course course);

    CourseUpdateResponse toCourseUpdateResponse(Course course);
}
