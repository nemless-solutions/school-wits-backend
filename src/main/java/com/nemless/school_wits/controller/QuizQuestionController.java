package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizQuestionDto;
import com.nemless.school_wits.dto.request.UpdateQuizQuestionDto;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.service.QuizQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz_question")
public class QuizQuestionController {
    private final QuizQuestionService quizQuestionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<QuizQuestion> createQuizQuestion(@Valid @RequestBody CreateQuizQuestionDto createQuizQuestionDto) {
        log.info("Creating quiz question: {}", createQuizQuestionDto);

        return ResponseEntity.ok(quizQuestionService.createQuizQuestion(createQuizQuestionDto));
    }

    @GetMapping("/{quizId}")
    ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizQuestionService.getQuestionsByQuizId(quizId));
    }

    @PutMapping("/{quizQuestionId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<QuizQuestion> updateQuizQuestion(@PathVariable Long quizQuestionId, @RequestBody UpdateQuizQuestionDto updateQuizQuestionDto) {
        log.info("Updating quiz question {}: {}", quizQuestionId, updateQuizQuestionDto);

        return ResponseEntity.ok(quizQuestionService.updateQuizQuestion(quizQuestionId, updateQuizQuestionDto));
    }

    @DeleteMapping("/{quizQuestionId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> deleteQuiz(@PathVariable Long quizQuestionId) {
        log.info("Deleting quiz question: {}", quizQuestionId);

        quizQuestionService.deleteQuizQuestion(quizQuestionId);

        return ResponseEntity.ok(ResponseMessage.QUIZ_QUESTION_DELETE_SUCCESSFUL);
    }
}
