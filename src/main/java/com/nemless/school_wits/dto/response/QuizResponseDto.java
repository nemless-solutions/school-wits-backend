package com.nemless.school_wits.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class QuizResponseDto {
    private Long id;
    private String title;
    private List<QuizQuestionResponse> questions;
    private Date createdAt;
}
