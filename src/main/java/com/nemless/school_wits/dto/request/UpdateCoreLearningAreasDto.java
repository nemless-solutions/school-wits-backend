package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateCoreLearningAreasDto {
    @NotNull(message = "Course id is required")
    private Long courseId;

    private String content;
}
