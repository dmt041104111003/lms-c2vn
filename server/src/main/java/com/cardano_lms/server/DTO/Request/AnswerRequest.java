package com.cardano_lms.server.DTO.Request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class AnswerRequest {
    private String content;
    private boolean isCorrect;
}
