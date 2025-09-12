package com.cardano_lms.server.DTO.Request;

import com.cardano_lms.server.Entity.InstructorProfile;
import jakarta.persistence.*;
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
public class CourseRequest {
    private String title;
    private String description;
    private String shortDescription;
    private String imageUrl;
    private boolean isDraft;
    private Double discount;
    private LocalDateTime discountEndTime;
    private String policyId;
    private Long instructorId;

    private List<CourseDetailRequest> details;
    private List<ChapterRequest> chapters;
    private List<Long> paymentMethodIds;
    private List<TestRequest> courseTests;

}
