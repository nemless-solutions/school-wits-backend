package com.nemless.school_wits.dto.request;

import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateCourseDto {
    private CourseMode mode;

    private CourseType type;

    private String title;

    private String description;

    private String uid;

    private float fee;
}
