package com.cardano_lms.server.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Table(name = "messages")
@Data @NoArgsConstructor @AllArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime sentAt;

    @ManyToOne @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne @JoinColumn(name = "course_id")
    private Course course;
}
