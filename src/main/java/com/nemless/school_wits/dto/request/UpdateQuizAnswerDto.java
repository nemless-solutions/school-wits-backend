package com.nemless.school_wits.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateQuizAnswerDto {
    private String title;
    private boolean isCorrect;
}
