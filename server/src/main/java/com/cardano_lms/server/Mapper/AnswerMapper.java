package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.AnswerRequest;
import com.cardano_lms.server.DTO.Response.AnswerResponse;
import com.cardano_lms.server.Entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    Answer toAnswer(AnswerRequest request);

    AnswerResponse toResponse(Answer answer);
}
