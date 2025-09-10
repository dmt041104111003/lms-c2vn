package com.cardano_lms.server.DTO.Response;

import com.cardano_lms.server.Entity.LoginMethod;
import com.cardano_lms.server.Entity.Role;
import com.cardano_lms.server.Validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
    String email;
    String walletAddress;
    LoginMethod loginMethod;
    Role role;
}

