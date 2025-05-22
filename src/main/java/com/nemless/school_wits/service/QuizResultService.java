package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.GenerateQuizResultDto;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.*;
import com.nemless.school_wits.repository.QuizRepository;
import com.nemless.school_wits.repository.QuizResultRepository;
import com.nemless.school_wits.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizResultService {
    private final QuizResultRepository quizResultRepository;
    private final QuizRepository quizRepository;
    private final AuthUtils authUtils;

    public List<QuizResult> getQuizResults() {
        return quizResultRepository.findByUser(authUtils.getAuthenticatedUser());
    }

    public QuizResult generateQuizResult(GenerateQuizResultDto generateQuizResultDto) {
        Quiz quiz = quizRepository.findById(generateQuizResultDto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));
        User user = authUtils.getAuthenticatedUser();
        if(quizResultRepository.existsByUserAndQuiz(user, quiz)) {
            log.info("Quiz {} result re-attempt by {}", quiz.getId(), user.getId());
            throw new BadRequestException(ResponseMessage.QUIZ_RESULT_EXISTS);
        }

        QuizResult quizResult = new QuizResult();
        quizResult.setQuiz(quiz);
        quizResult.setUser(user);
        quizResult.setAnswers(
                calculateMark(quiz.getQuestions(), generateQuizResultDto.getQuestionAnswers())
        );
        return quizResultRepository.save(quizResult);
    }

    private Map<Long, Boolean> calculateMark(List<QuizQuestion> questions, Map<Long, Long> questionAnswers) {
        Map<Long, Boolean> answers = new HashMap<>();

        for (Map.Entry<Long, Long> entry : questionAnswers.entrySet()) {
            Long questionId = entry.getKey();
            Long providedAnswerId = entry.getValue();

            QuizQuestion question = questions.stream()
                    .filter(q -> q.getId().equals(questionId))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(ResponseMessage.INVALID_QUESTION_ANSWER));

            QuizAnswer answer = question.getAnswers().stream()
                    .filter(a -> a.getId().equals(providedAnswerId))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(ResponseMessage.INVALID_QUESTION_ANSWER));

            answers.put(questionId, answer.isCorrect());
        }

        return answers;
    }
}
