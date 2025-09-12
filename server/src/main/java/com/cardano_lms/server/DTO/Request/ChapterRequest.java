package com.cardano_lms.server.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterRequest {
    private String title;
    private Integer orderIndex;
    private List<LectureRequest> lectures;
    private List<TestRequest> tests;
}
