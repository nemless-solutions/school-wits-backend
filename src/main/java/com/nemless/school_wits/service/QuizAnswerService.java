package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizAnswerDto;
import com.nemless.school_wits.dto.request.UpdateQuizAnswerDto;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.QuizAnswer;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.repository.QuizAnswerRepository;
import com.nemless.school_wits.repository.QuizQuestionRepository;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizAnswerService {
    private final QuizAnswerRepository quizAnswerRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    @Transactional
    public QuizAnswer createQuizAnswer(CreateQuizAnswerDto createQuizAnswerDto) {
        QuizQuestion question = quizQuestionRepository.findById(createQuizAnswerDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ID));

        QuizAnswer answer = new QuizAnswer();
        answer.setQuestion(question);
        answer.setTitle(createQuizAnswerDto.getTitle());
        answer.setCorrect(createQuizAnswerDto.isCorrect());
        answer = quizAnswerRepository.save(answer);

        question.getAnswers().add(answer);
        quizQuestionRepository.save(question);
        return answer;
    }

    public List<QuizAnswer> getAnswersByQuestionId(Long questionId) {
        QuizQuestion question = quizQuestionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ID));

        return question.getAnswers();
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
