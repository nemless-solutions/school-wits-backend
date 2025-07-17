package com.nemless.school_wits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Getter
public class QuizAnswerAdminResponse extends QuizAnswerResponse {
    private boolean isCorrect;
}
