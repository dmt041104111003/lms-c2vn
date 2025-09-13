package com.cardano_lms.server.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCreationRequest {
    private String title;
    private String description;
    private String shortDescription;
    private String requirement;
    private String imageUrl;
    private boolean isDraft;
    private Double discount;
    private Integer price;
    private LocalDateTime discountEndTime;
    private String policyId;
    private Long instructorId;


    private List<ChapterRequest> chapters;
    private List<String> paymentMethodIds;
    private List<TestRequest> courseTests;

}
