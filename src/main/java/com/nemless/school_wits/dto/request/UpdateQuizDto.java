package com.nemless.school_wits.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateQuizDto {
    private String title;
    private int questionMark;
    private int duration;
}
