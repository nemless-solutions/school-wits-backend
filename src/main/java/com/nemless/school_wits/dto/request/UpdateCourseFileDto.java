package com.nemless.school_wits.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateCourseFileDto {
    private String title;
    private String description;
}
