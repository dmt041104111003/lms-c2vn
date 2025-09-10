package com.cardano_lms.server.Repository;

import com.cardano_lms.server.Entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    List<SocialLink> findByInstructorId(Long instructorId);
}