package com.cardano_lms.server.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;
    private String shortDescription;
    private String requirement;
    private String imageUrl;
    private boolean isDraft;
    private Integer price;
    private Double discount;
    private LocalDateTime discountEndTime;
    private String policyId;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorProfile instructor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Chapter> chapters = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> courseTests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursePaymentMethod> coursePaymentMethods = new ArrayList<>();

    public void addTest(Test test) {
        courseTests.add(test);
        test.setCourse(this);
    }
    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
        chapter.setCourse(this);
    }
}
