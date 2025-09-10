package com.cardano_lms.server.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "course_payment_methods")
@Data @NoArgsConstructor @AllArgsConstructor
public class CoursePaymentMethod {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
}
