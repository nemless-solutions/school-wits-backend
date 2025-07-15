package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizDto;
import com.nemless.school_wits.dto.request.UpdateQuizDto;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.service.QuizService;
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
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Quiz> createQuiz(@Valid @RequestBody CreateQuizDto createQuizDto) {
        log.info("Creating quiz: {}", createQuizDto);

        return ResponseEntity.ok(quizService.createQuiz(createQuizDto));
    }

    @GetMapping("/{videoId}")
    ResponseEntity<List<Quiz>> getQuizzesByVideoId(@PathVariable Long videoId) {
        return ResponseEntity.ok(quizService.getQuizzesByVideoId(videoId));
    }

    @PutMapping("/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody UpdateQuizDto updateQuizDto) {
        log.info("Updating quiz {}: {}", quizId, updateQuizDto);

        return ResponseEntity.ok(quizService.updateQuiz(quizId, updateQuizDto));
    }

    @DeleteMapping("/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> deleteQuiz(@PathVariable Long quizId) {
        log.info("Deleting quiz: {}", quizId);

        quizService.deleteQuiz(quizId);

        return ResponseEntity.ok(ResponseMessage.QUIZ_DELETE_SUCCESSFUL);
    }
}
