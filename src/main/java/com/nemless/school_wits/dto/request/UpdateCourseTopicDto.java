package com.nemless.school_wits.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateCourseTopicDto {
    private String title;
    private String description;
    private boolean isLocked;
}
