package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizQuestionDto;
import com.nemless.school_wits.dto.request.UpdateQuizQuestionDto;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.repository.QuizQuestionRepository;
import com.nemless.school_wits.repository.QuizRepository;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizQuestionService {
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizRepository quizRepository;
    private final EnrolledCourseService enrolledCourseService;

    @Transactional
    public QuizQuestion createQuizQuestion(CreateQuizQuestionDto createQuizQuestionDto) {
        Quiz quiz = quizRepository.findById(createQuizQuestionDto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));

        QuizQuestion question = new QuizQuestion();
        question.setQuiz(quiz);
        question.setTitle(createQuizQuestionDto.getTitle());

        return quizQuestionRepository.save(question);
    }

    public List<QuizQuestion> getQuestionsByQuizId(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));

        enrolledCourseService.validateCourseMaterialAccess(quiz.getVideo().getCourseTopic().getCourse());

        return quiz.getQuestions();
    }

    public QuizQuestion updateQuizQuestion(Long quizQuestionId, UpdateQuizQuestionDto updateQuizQuestionDto) {
        QuizQuestion question = quizQuestionRepository.findById(quizQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ID));

        if(!StringUtils.isEmpty(updateQuizQuestionDto.getTitle()) && !updateQuizQuestionDto.getTitle().equals(question.getTitle())) {
            question.setTitle(updateQuizQuestionDto.getTitle());
        }

        return quizQuestionRepository.save(question);
    }

    @Transactional
    public void deleteQuizQuestion(Long quizQuestionId) {
        QuizQuestion question = quizQuestionRepository.findById(quizQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUESTION_ID));

        quizQuestionRepository.delete(question);
    }
}
