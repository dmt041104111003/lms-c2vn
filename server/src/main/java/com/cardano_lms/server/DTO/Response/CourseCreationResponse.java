package com.cardano_lms.server.DTO.Response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCreationResponse {
    private String id;
    private String title;
    private String description;
    private String shortDescription;
    private String requirement;
    private String imageUrl;
    private boolean isDraft;
    private Integer price;
    private Double discount;
    private LocalDateTime discountEndTime;
    private String policyId;

    private InstructorProfileResponse instructorProfileResponse;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ChapterResponse> chapters;
    private List<TestResponse> courseTests;
}
