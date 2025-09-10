package com.cardano_lms.server.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true,nullable = true)
    String username;
    String password;

    @Column(unique = true,nullable = true)
    String email;

    String firstName;
    LocalDate dob;
    String lastName;

    @Column(unique = true,nullable = true)
    String walletAddress;

    @ManyToOne
    @JoinColumn(name = "role_name")
    Role role;

    @ManyToOne
    @JoinColumn(name = "login_method_name")
    LoginMethod loginMethod;


}
