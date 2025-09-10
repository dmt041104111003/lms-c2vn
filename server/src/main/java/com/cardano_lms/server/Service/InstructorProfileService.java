package com.cardano_lms.server.Service;

import com.cardano_lms.server.DTO.Request.InstructorProfileUpdateRequest;
import com.cardano_lms.server.DTO.Request.SocialLinkRequest;
import com.cardano_lms.server.DTO.Response.InstructorProfileResponse;
import com.cardano_lms.server.Entity.InstructorProfile;
import com.cardano_lms.server.Entity.SocialLink;
import com.cardano_lms.server.Exception.AppException;
import com.cardano_lms.server.Exception.ErrorCode;
import com.cardano_lms.server.Mapper.InstructorProfileMapper;
import com.cardano_lms.server.Repository.InstructorProfileRepository;
import com.cardano_lms.server.Repository.SocialLinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorProfileService {

    private final InstructorProfileRepository instructorRepository;
    private final InstructorProfileMapper instructorProfileMapper;
    private final SocialLinkRepository socialLinkRepository;

    @Transactional
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public InstructorProfileResponse getProfileByUserId(String userId) {
        InstructorProfile profile = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<SocialLink> links = socialLinkRepository.findByInstructorId(profile.getId());

        InstructorProfileResponse response = instructorProfileMapper.toResponse(profile);
        response.setSocialLinks(
                links.stream().map(instructorProfileMapper::toResponse).toList()
        );

        return response;
    }


    @Transactional
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public InstructorProfileResponse updateProfile(Long instructorId, InstructorProfileUpdateRequest request) {
        InstructorProfile profile = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        instructorProfileMapper.updateProfileFromRequest(request, profile);

        if (request.getSocialLinks() != null) {
            syncSocialLinks(profile, request.getSocialLinks());
        }

        List<SocialLink> links = socialLinkRepository.findByInstructorId(instructorId);
        return InstructorProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .bio(profile.getBio())
                .expertise(profile.getExpertise())
                .socialLinks(
                        links.stream()
                                .map(instructorProfileMapper::toResponse)
                                .toList()
                )
                .build();
    }
    private void syncSocialLinks(InstructorProfile profile, List<SocialLinkRequest> requests) {
        List<SocialLink> existingLinks = socialLinkRepository.findByInstructorId(profile.getId());

        Map<String, SocialLink> existingMap = existingLinks.stream()
                .collect(Collectors.toMap(SocialLink::getName, Function.identity()));

        Set<String> requestedNames = requests.stream()
                .map(SocialLinkRequest::getName)
                .collect(Collectors.toSet());

        existingLinks.stream()
                .filter(link -> !requestedNames.contains(link.getName()))
                .forEach(socialLinkRepository::delete);

        for (SocialLinkRequest req : requests) {
            SocialLink existing = existingMap.get(req.getName());
            if (existing != null) {
                // update URL
                existing.setUrl(req.getUrl());
                socialLinkRepository.save(existing);
            } else {
                SocialLink newLink = SocialLink.builder()
                        .name(req.getName())
                        .url(req.getUrl())
                        .instructor(profile)
                        .build();
                socialLinkRepository.save(newLink);
            }
        }

        log.info("Synced social links for instructor id={}, now total={}",
                profile.getId(), socialLinkRepository.findByInstructorId(profile.getId()).size());
    }




}


