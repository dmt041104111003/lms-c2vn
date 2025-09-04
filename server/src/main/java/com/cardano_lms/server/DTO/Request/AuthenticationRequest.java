package com.cardano_lms.server.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    String username;
    String password;
    String email;
    String address;
    String signature;
    String key;
    String nonce;
    String loginMethod;
}
