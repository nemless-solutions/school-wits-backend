package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.GenerateQuizResultDto;
import com.nemless.school_wits.model.QuizResult;
import com.nemless.school_wits.service.QuizResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz_result")
public class QuizResultController {
    private final QuizResultService quizResultService;

    @PostMapping
    ResponseEntity<QuizResult> generateQuizResult(@Valid @RequestBody GenerateQuizResultDto generateQuizResultDto) {
        log.info("Generating quiz result: {}", generateQuizResultDto);

        return ResponseEntity.ok(quizResultService.generateQuizResult(generateQuizResultDto));
    }

    @GetMapping
    ResponseEntity<List<QuizResult>> getQuizResults() {
        return ResponseEntity.ok(quizResultService.getQuizResults());
    }

    @GetMapping("/{quizResultId}")
    ResponseEntity<QuizResult> getQuizResult(@PathVariable Long quizResultId) {
        return ResponseEntity.ok(quizResultService.getQuizResult(quizResultId));
    }
}
