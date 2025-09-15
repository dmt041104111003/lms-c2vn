package com.cardano_lms.server.Controller;

import com.cardano_lms.server.DTO.Request.ApiResponse;
import com.cardano_lms.server.DTO.Request.EnrollCourseRequest;
import com.cardano_lms.server.DTO.Request.ValidatePaymentRequest;
import com.cardano_lms.server.Entity.Enrollment;
import com.cardano_lms.server.Service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollment")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @GetMapping("/validate")
    public boolean checkValidPayment(@RequestBody ValidatePaymentRequest request) {
        return enrollmentService.verifyPayment(request.getSender(),request.getReceiver(),request.getAmount(),request.getTxHash());
    }

    @PostMapping
    public ApiResponse<Enrollment> enrollACourse(@RequestBody EnrollCourseRequest request) {
        Enrollment enrollment = enrollmentService.createEnrollmentAfterPayment(
                request.getUserId(),
                request.getCourseId(),
                request.getCoursePaymentMethodId(),
                request.getPriceAda(),
                request.getTxHash()
        );

        return ApiResponse.<Enrollment>builder()
                .message("Enroll this course success")
                .result(enrollment)
                .build();
    }

}
