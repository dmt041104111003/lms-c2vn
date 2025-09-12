package com.cardano_lms.server.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lectures")
@Data @NoArgsConstructor @AllArgsConstructor
public class Lecture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String videoUrl;
    private int time;
    private int orderIndex;
    private String resourceUrl;
    private String resourceType;

    @ManyToOne @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
