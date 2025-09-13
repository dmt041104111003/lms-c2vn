package com.cardano_lms.server.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_methods")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentMethod {
    @Id
    private String name;

    private String description;
    private String currency;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursePaymentMethod> coursePaymentMethods = new ArrayList<>();
}
