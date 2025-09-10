package com.cardano_lms.server.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;
    private String shortDescription;
    private String imageUrl;
    private boolean isDraft;
    private Double discount;
    private LocalDate discountEndTime;
    private String policyId;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private InstructorProfile instructor;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
