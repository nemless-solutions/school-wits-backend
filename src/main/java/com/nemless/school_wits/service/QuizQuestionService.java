package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizQuestionDto;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.repository.QuizQuestionRepository;
import com.nemless.school_wits.repository.QuizRepository;
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
        question = quizQuestionRepository.save(question);

        quiz.getQuestions().add(question);
        quizRepository.save(quiz);
        return question;
    }

    public List<QuizQuestion> getQuestionsByQuizId(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));

        enrolledCourseService.validateCourseMaterialAccess(quiz.getVideo().getCourseTopic().getCourse());

        return quiz.getQuestions();
    }
}
