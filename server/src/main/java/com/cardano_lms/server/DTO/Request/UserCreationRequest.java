package com.cardano_lms.server.DTO.Request;

import com.cardano_lms.server.Validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    String email;
    String walletAddress;
    String loginMethod;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;
}
