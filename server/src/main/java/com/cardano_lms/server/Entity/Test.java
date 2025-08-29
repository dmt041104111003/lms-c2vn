package com.cardano_lms.server.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "certificate_id")
    private String certificateId;

}