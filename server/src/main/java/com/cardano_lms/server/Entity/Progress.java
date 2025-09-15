package com.cardano_lms.server.Entity;

import com.cardano_lms.server.Constant.CourseContentType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "progress")
@Data @NoArgsConstructor @AllArgsConstructor
public class Progress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CourseContentType type;

    private int score;
    private LocalDate completedAt;

    @ManyToOne @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne @JoinColumn(name = "test_id")
    private Test test;
}
