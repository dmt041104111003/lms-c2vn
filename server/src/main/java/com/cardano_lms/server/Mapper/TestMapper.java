package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.TestRequest;
import com.cardano_lms.server.DTO.Response.TestResponse;
import com.cardano_lms.server.Entity.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface TestMapper {
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "questions", ignore = true)
    Test toEntity(TestRequest request);

    TestResponse toResponse(Test test);
    List<TestResponse> toResponseList(List<Test> entities);
}
