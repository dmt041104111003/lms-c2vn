package com.cardano_lms.server.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorProfileResponse {
    private Long id;
    private String bio;
    private String expertise;
    private String userId;
    private List<SocialLinkResponse> socialLinks;
}