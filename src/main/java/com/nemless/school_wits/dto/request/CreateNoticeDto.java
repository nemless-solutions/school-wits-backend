package com.nemless.school_wits.dto.request;

import com.nemless.school_wits.enums.Grade;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateNoticeDto {
    @NotBlank
    private String title;
    private String details;
    private boolean notifyAll;
    private Grade grade;
    private Long userId;
    private Long courseId;
}
