package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.CreateQuizDto;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    ResponseEntity<Quiz> createQuiz(CreateQuizDto createQuizDto) {
        log.info("Creating quiz: {}", createQuizDto);

        return ResponseEntity.ok(quizService.createQuiz(createQuizDto));
    }

    @GetMapping("/{videoId}")
    ResponseEntity<Quiz> getQuizByVideoId(@PathVariable Long videoId) {
        return ResponseEntity.ok(quizService.getQuizByVideoId(videoId));
    }
}
