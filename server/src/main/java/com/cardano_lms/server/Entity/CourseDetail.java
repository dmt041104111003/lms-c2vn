package com.cardano_lms.server.Entity;

import com.cardano_lms.server.Constant.CourseDetailType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_details")
@Data @NoArgsConstructor @AllArgsConstructor
public class CourseDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CourseDetailType type;

    private String content;

    @ManyToOne @JoinColumn(name = "course_id")
    private Course course;
}
