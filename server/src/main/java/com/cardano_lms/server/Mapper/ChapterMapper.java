package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.ChapterRequest;
import com.cardano_lms.server.DTO.Response.ChapterResponse;
import com.cardano_lms.server.Entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "tests", ignore = true)
    Chapter toEntity(ChapterRequest request);

    ChapterResponse toResponse(Chapter chapter);
}
