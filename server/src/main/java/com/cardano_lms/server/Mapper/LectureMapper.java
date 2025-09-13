package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.LectureRequest;
import com.cardano_lms.server.Entity.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LectureMapper {
    @Mapping(target = "previewFree", defaultValue = "false")
    Lecture toEntity(LectureRequest request);
}
