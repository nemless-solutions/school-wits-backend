package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateQuizDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Video id is required")
    private Long videoId;

    @NotNull(message = "Mark of each question is required")
    private int questionMark;

    private int duration;
}
