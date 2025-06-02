package com.nemless.school_wits.dto.request;

import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.CourseType;
import com.nemless.school_wits.enums.Grade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateCourseDto {
    @NotNull
    private Grade grade;

    @NotNull
    private CourseMode mode;

    @NotNull
    private CourseType type;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "UID is required")
    private String uid;

    @NotNull(message = "Fee is required")
    private float fee;
}
