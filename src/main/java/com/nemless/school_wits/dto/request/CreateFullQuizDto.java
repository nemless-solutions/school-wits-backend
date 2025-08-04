package com.nemless.school_wits.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class CreateFullQuizDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Video id is required")
    private Long videoId;

    private int questionMark;

    private int duration;

    @Valid
    private List<CreateFullQuizQuestionDto> questions;
}
