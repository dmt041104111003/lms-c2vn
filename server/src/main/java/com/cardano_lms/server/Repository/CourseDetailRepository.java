package com.cardano_lms.server.Repository;

import com.cardano_lms.server.Entity.CourseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {}
