package com.cardano_lms.server.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRequest {
    private String title;
    private String videoUrl;
    private Integer duration;
    private Integer orderIndex;
    private String resourceUrl;
    private String resourceType;
}
