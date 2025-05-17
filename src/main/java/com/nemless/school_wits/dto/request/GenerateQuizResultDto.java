package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class GenerateQuizResultDto {
    @NotNull(message = "Quiz id is required")
    private Long quizId;

    @NotNull(message = "Question answers map is required")
    @Size(min = 1, message = "At least one answer must be provided")
    private Map<Long, Long> questionAnswers;
}
