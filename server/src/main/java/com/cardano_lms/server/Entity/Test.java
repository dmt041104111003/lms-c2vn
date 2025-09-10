package com.cardano_lms.server.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tests")
@Data @NoArgsConstructor @AllArgsConstructor
public class Test {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int durationMinutes;
    private String rule;
    private int passScore;

    @ManyToOne @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;
}
