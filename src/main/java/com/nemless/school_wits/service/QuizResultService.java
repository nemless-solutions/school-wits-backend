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

        int mark = calculateMark(quiz.getQuestions(), generateQuizResultDto.getQuestionAnswers());
        User user = authUtils.getAuthenticatedUser();
        QuizResult quizResult = new QuizResult();
        quizResult.setQuiz(quiz);
        quizResult.setUser(user);
        quizResult.setMark(mark);
        return quizResultRepository.save(quizResult);
    }

    private int calculateMark(List<QuizQuestion> questions, Map<Long, Long> questionAnswers) {
        int mark = 0;

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

            if (answer.isCorrect()) {
                mark++;
            }
        }

        return mark;
    }
}
