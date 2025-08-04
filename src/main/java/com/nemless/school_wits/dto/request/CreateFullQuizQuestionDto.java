package com.nemless.school_wits.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class CreateFullQuizQuestionDto {
    @NotBlank(message = "Title is required")
    private String title;

    @Valid
    private List<CreateFullQuizAnswerDto> answers;
}
