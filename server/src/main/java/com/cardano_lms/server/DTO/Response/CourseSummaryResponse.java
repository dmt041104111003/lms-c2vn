package com.cardano_lms.server.DTO.Response;

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
public class CourseSummaryResponse {
    private String id;
    private String title;
    private String description;
    private String shortDescription;
    private String requirement;
    private String imageUrl;
    private boolean isDraft;
    private Double discount;
    private LocalDateTime discountEndTime;
    private String policyId;
    private String instructorName;
}
