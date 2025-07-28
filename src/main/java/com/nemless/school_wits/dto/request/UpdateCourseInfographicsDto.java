package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class UpdateCourseInfographicsDto {
    @NotEmpty(message = "Chart values must not be empty")
    private Map<String, Integer> chartValues;
}
