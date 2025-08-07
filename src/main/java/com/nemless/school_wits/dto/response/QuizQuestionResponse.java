package com.nemless.school_wits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuizQuestionResponse {
    private Long id;
    private String title;
    private List<QuizAnswerResponse> answers;
}
