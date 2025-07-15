package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizAnswerDto;
import com.nemless.school_wits.dto.request.UpdateQuizAnswerDto;
import com.nemless.school_wits.model.QuizAnswer;
import com.nemless.school_wits.service.QuizAnswerService;
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
@RequestMapping("/quiz_answer")
public class QuizAnswerController {
    private final QuizAnswerService quizAnswerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<QuizAnswer> createQuizAnswer(@Valid @RequestBody CreateQuizAnswerDto createQuizAnswerDto) {
        log.info("Creating quiz answer: {}", createQuizAnswerDto);

        return ResponseEntity.ok(quizAnswerService.createQuizAnswer(createQuizAnswerDto));
    }

    @GetMapping("/{questionId}")
    ResponseEntity<List<QuizAnswer>> getQuizAnswers(@PathVariable Long questionId) {
        return ResponseEntity.ok(quizAnswerService.getAnswersByQuestionId(questionId));
    }

    @PutMapping("/{quizAnswerId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<QuizAnswer> updateQuizAnswer(@PathVariable Long quizAnswerId, @RequestBody UpdateQuizAnswerDto updateQuizAnswerDto) {
        log.info("Updating quiz answer {}: {}", quizAnswerId, updateQuizAnswerDto);

        return ResponseEntity.ok(quizAnswerService.updateQuizAnswer(quizAnswerId, updateQuizAnswerDto));
    }

    @DeleteMapping("/{quizAnswerId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> deleteQuizAnswer(@PathVariable Long quizAnswerId) {
        log.info("Deleting quiz answer: {}", quizAnswerId);

        quizAnswerService.deleteQuizAnswer(quizAnswerId);

        return ResponseEntity.ok(ResponseMessage.QUIZ_ANSWER_DELETE_SUCCESSFUL);
    }
}
