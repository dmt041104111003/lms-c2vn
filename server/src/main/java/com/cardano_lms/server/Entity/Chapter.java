package com.cardano_lms.server.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "chapters")
@Data @NoArgsConstructor @AllArgsConstructor
public class Chapter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int index;

    @ManyToOne @JoinColumn(name = "course_id")
    private Course course;
}
