package com.cardano_lms.server.Repository;

import com.cardano_lms.server.Entity.InstructorProfile;
import com.cardano_lms.server.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, Long> {
    Optional<InstructorProfile> findByUser(User user);
    Optional<InstructorProfile> findByUserId(String id);
}
