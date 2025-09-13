package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.QuestionRequest;
import com.cardano_lms.server.Entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AnswerMapper.class)
public interface QuestionMapper {
    @Mapping(target = "test", ignore = true)
    @Mapping(target = "answers", ignore = true)
    Question toQuestion(QuestionRequest request);

}