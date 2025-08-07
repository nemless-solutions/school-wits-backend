package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateFullQuizDto;
import com.nemless.school_wits.dto.request.CreateQuizDto;
import com.nemless.school_wits.dto.request.UpdateQuizDto;
import com.nemless.school_wits.dto.response.QuizResponseDto;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/full")
    ResponseEntity<Quiz> createFullQuiz(@Valid @RequestBody CreateFullQuizDto createFullQuizDto) {
        log.info("Creating quiz with question and answers: {}", createFullQuizDto);

        return ResponseEntity.ok(quizService.createFullQuiz(createFullQuizDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/full")
    ResponseEntity<Quiz> updateFullQuiz(@Valid @RequestBody Quiz quiz) {
        log.info("Updating quiz with question and answers: {}", quiz);

        return ResponseEntity.ok(quizService.updateFullQuiz(quiz));
    }

    @GetMapping("/video/{videoId}")
    ResponseEntity<List<Quiz>> getQuizzesByVideoId(@PathVariable Long videoId) {
        return ResponseEntity.ok(quizService.getQuizzesByVideoId(videoId));
    }

    @GetMapping("/{quizId}")
    ResponseEntity<QuizResponseDto> getQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuiz(quizId));
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
