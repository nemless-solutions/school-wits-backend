package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateFullQuizAnswerDto {
    @NotBlank(message = "Title is required")
    private String title;

    private boolean isCorrect;
}
