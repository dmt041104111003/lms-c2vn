package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.InstructorProfileUpdateRequest;
import com.cardano_lms.server.DTO.Request.SocialLinkRequest;
import com.cardano_lms.server.DTO.Response.InstructorProfileResponse;
import com.cardano_lms.server.DTO.Response.SocialLinkResponse;
import com.cardano_lms.server.Entity.InstructorProfile;
import com.cardano_lms.server.Entity.SocialLink;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InstructorProfileMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileFromRequest(InstructorProfileUpdateRequest request,
                                  @MappingTarget InstructorProfile profile);

    SocialLink toSocialLink(SocialLinkRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSocialLinkFromRequest(SocialLinkRequest request,
                                     @MappingTarget SocialLink socialLink);

    @Mapping(source = "user.id", target = "userId")
    InstructorProfileResponse toResponse(InstructorProfile profile);

    SocialLinkResponse toResponse(SocialLink socialLink);
}
