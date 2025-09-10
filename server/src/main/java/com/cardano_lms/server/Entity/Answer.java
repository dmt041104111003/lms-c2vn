package com.cardano_lms.server.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "answers")
@Data @NoArgsConstructor @AllArgsConstructor
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private boolean isCorrect;

    @ManyToOne @JoinColumn(name = "question_id")
    private Question question;
}
