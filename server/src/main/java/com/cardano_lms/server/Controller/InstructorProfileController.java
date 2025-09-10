package com.cardano_lms.server.Controller;

import com.cardano_lms.server.DTO.Request.ApiResponse;
import com.cardano_lms.server.DTO.Request.InstructorProfileUpdateRequest;
import com.cardano_lms.server.DTO.Response.InstructorProfileResponse;
import com.cardano_lms.server.Entity.InstructorProfile;
import com.cardano_lms.server.Service.InstructorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instructor-profiles")
@RequiredArgsConstructor
public class InstructorProfileController {

    private final InstructorProfileService profileService;

    @GetMapping("/{userId}")
    public ApiResponse<InstructorProfileResponse> getDetailInstructorProfiles(
            @PathVariable String userId
    ) {
        InstructorProfileResponse response = profileService.getProfileByUserId(userId);
        return
                ApiResponse.<InstructorProfileResponse>builder()
                        .message("Profile detail")
                        .result(response)
                        .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<InstructorProfileResponse> updateProfile(
            @PathVariable Long id,
            @RequestBody InstructorProfileUpdateRequest request) {

        InstructorProfileResponse response = profileService.updateProfile(id, request);

        return
                ApiResponse.<InstructorProfileResponse>builder()
                        .message("Profile updated successfully")
                        .result(response)
                        .build();
    }

}
