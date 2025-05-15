package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateQuizDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Video id is required")
    private Long videoId;

    @NotBlank(message = "Total mark is required")
    private int totalMark;

    @NotBlank(message = "Quiz duration is required")
    private int duration;
}
