package com.cardano_lms.server.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "lectures")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Lecture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String videoUrl;
    private int time;
    private int orderIndex;
    private String resourceUrl;
    private String resourceType;
    private Boolean previewFree;

    @ManyToOne @JoinColumn(name = "chapter_id")
    @JsonIgnore
    private Chapter chapter;
}
