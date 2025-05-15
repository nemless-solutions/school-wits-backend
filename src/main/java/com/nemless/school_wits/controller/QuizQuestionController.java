package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.CreateQuizQuestionDto;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz_question")
public class QuizQuestionController {
    private final QuizQuestionService quizQuestionService;

    @PostMapping
    ResponseEntity<QuizQuestion> createQuizQuestion(CreateQuizQuestionDto createQuizQuestionDto) {
        log.info("Creating quiz question: {}", createQuizQuestionDto);

        return ResponseEntity.ok(quizQuestionService.createQuizQuestion(createQuizQuestionDto));
    }

    @GetMapping("/{quizId}")
    ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizQuestionService.getQuestionsByQuizId(quizId));
    }
}
