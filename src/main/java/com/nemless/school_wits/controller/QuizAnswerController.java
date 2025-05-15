package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.CreateQuizAnswerDto;
import com.nemless.school_wits.model.QuizAnswer;
import com.nemless.school_wits.service.QuizAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz_answer")
public class QuizAnswerController {
    private final QuizAnswerService quizAnswerService;

    @PostMapping
    ResponseEntity<QuizAnswer> createQuizAnswer(CreateQuizAnswerDto createQuizAnswerDto) {
        log.info("Creating quiz answer: {}", createQuizAnswerDto);

        return ResponseEntity.ok(quizAnswerService.createQuizAnswer(createQuizAnswerDto));
    }

    @GetMapping("/{questionId}")
    ResponseEntity<List<QuizAnswer>> getQuizAnswers(@PathVariable Long questionId) {
        return ResponseEntity.ok(quizAnswerService.getAnswersByQuestionId(questionId));
    }
}
