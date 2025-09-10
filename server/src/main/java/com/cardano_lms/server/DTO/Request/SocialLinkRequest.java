package com.cardano_lms.server.DTO.Request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialLinkRequest {
    String name;
    String url;
}
