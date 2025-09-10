package com.cardano_lms.server.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Data @NoArgsConstructor @AllArgsConstructor
public class Certificate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime issuedAt;
    private String txHash;
    private String imgUrl;
    private String policyId;

    @OneToOne @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;
}
