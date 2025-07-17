package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizAnswerDto;
import com.nemless.school_wits.dto.request.UpdateQuizAnswerDto;
import com.nemless.school_wits.dto.response.QuizAnswerAdminResponse;
import com.nemless.school_wits.dto.response.QuizAnswerResponse;
import com.nemless.school_wits.enums.Role;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.QuizAnswer;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.repository.QuizAnswerRepository;
import com.nemless.school_wits.repository.QuizQuestionRepository;
import com.nemless.school_wits.util.AuthUtils;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizAnswerService {
    private final QuizAnswerRepository quizAnswerRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final AuthUtils authUtils;

    @Transactional
    public QuizAnswer createQuizAnswer(CreateQuizAnswerDto createQuizAnswerDto) {
        QuizQuestion question = quizQuestionRepository.findById(createQuizAnswerDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ID));

        QuizAnswer answer = new QuizAnswer();
        answer.setQuestion(question);
        answer.setTitle(createQuizAnswerDto.getTitle());
        answer.setCorrect(createQuizAnswerDto.isCorrect());

        return quizAnswerRepository.save(answer);
    }

    public List<QuizAnswerResponse> getAnswersByQuestionId(Long questionId) {
        QuizQuestion question = quizQuestionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ID));

        boolean hasPermissionToViewCorrectAnswer = authUtils.getAuthenticatedUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName() == Role.ROLE_ADMIN || role.getName() == Role.ROLE_TEACHER);

        List<QuizAnswer> answers = question.getAnswers();
        if(hasPermissionToViewCorrectAnswer) {
            return answers.stream()
                    .map(answer -> QuizAnswerAdminResponse.builder()
                            .id(answer.getId())
                            .title(answer.getTitle())
                            .isCorrect(answer.isCorrect())
                            .build())
                    .collect(Collectors.toList());
        } else {
            return answers.stream()
                    .map(answer -> QuizAnswerResponse.builder()
                            .id(answer.getId())
                            .title(answer.getTitle())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public QuizAnswer updateQuizAnswer(Long quizAnswerId, UpdateQuizAnswerDto updateQuizAnswerDto) {
        QuizAnswer answer = quizAnswerRepository.findById(quizAnswerId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ANSWER));

        if(!StringUtils.isEmpty(updateQuizAnswerDto.getTitle()) && !updateQuizAnswerDto.getTitle().equals(answer.getTitle())) {
            answer.setTitle(updateQuizAnswerDto.getTitle());
        }
        if(answer.isCorrect() != updateQuizAnswerDto.isCorrect()) {
            if(updateQuizAnswerDto.isCorrect()) {
                QuizAnswer correctAnswer = quizAnswerRepository.findByQuestionAndIsCorrect(answer.getQuestion(), true)
                        .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ANSWER));
                correctAnswer.setCorrect(false);
                quizAnswerRepository.save(correctAnswer);
                answer.setCorrect(true);
            } else {
                throw new BadRequestException(ResponseMessage.CORRECT_ANSWER_REQUIRED);
            }
        }

        return quizAnswerRepository.save(answer);
    }

    @Transactional
    public void deleteQuizAnswer(Long quizAnswerId) {
        QuizAnswer answer = quizAnswerRepository.findById(quizAnswerId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ANSWER));

        if(answer.isCorrect()) {
            throw new BadRequestException(ResponseMessage.CORRECT_ANSWER_REQUIRED);
        }

        quizAnswerRepository.delete(answer);
    }
}
