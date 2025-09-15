package com.cardano_lms.server.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollCourseRequest {
    private String userId;
    private String courseId;
    private Long coursePaymentMethodId;
    private double priceAda;
    private String txHash;
}
