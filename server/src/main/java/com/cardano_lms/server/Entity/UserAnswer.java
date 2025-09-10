package com.cardano_lms.server.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_answers")
@Data @NoArgsConstructor @AllArgsConstructor
public class UserAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne @JoinColumn(name = "answer_id")
    private Answer answer;
}
