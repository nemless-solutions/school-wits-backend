package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.*;
import com.nemless.school_wits.enums.CourseFileType;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.model.QuizAnswer;
import com.nemless.school_wits.model.QuizQuestion;
import com.nemless.school_wits.repository.CourseFileRepository;
import com.nemless.school_wits.repository.QuizAnswerRepository;
import com.nemless.school_wits.repository.QuizQuestionRepository;
import com.nemless.school_wits.repository.QuizRepository;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final CourseFileRepository courseFileRepository;
    private final EnrolledCourseService enrolledCourseService;

    private static final int DEFAULT_QUESTION_MARK = 5;
    private static final int DEFAULT_QUIZ_DURATION = 5;

    @Transactional
    public Quiz createQuiz(CreateQuizDto createQuizDto) {
        CourseFile courseFile = validateQuizCourseFile(createQuizDto.getVideoId());

        Quiz quiz = Quiz.builder()
                .video(courseFile)
                .title(createQuizDto.getTitle())
                .questionMark(DEFAULT_QUESTION_MARK)
                .duration(DEFAULT_QUIZ_DURATION)
                .build();

        return quizRepository.save(quiz);
    }

    @Transactional
    public Quiz createFullQuiz(CreateFullQuizDto createFullQuizDto) {
        CourseFile courseFile = validateQuizCourseFile(createFullQuizDto.getVideoId());
        validateCorrectQuizAnswers(createFullQuizDto.getQuestions());

        Quiz quiz = quizRepository.save(
                Quiz.builder()
                .video(courseFile)
                .title(createFullQuizDto.getTitle())
                .questionMark(DEFAULT_QUESTION_MARK)
                .duration(DEFAULT_QUESTION_MARK)
                .build()
        );

        List<QuizQuestion> questions = new ArrayList<>();
        for(CreateFullQuizQuestionDto question : createFullQuizDto.getQuestions()) {
            QuizQuestion quizQuestion = quizQuestionRepository.save(
                    QuizQuestion.builder()
                            .quiz(quiz)
                            .title(question.getTitle())
                            .build()
            );
            questions.add(quizQuestion);

            List<QuizAnswer> answers = new ArrayList<>();
            for(CreateFullQuizAnswerDto answer : question.getAnswers()) {
                answers.add(
                    QuizAnswer.builder()
                            .question(quizQuestion)
                            .title(answer.getTitle())
                            .isCorrect(answer.isCorrect())
                            .build()
                );
            }

            answers = quizAnswerRepository.saveAll(answers);
            quizQuestion.setAnswers(answers);
        }
        quiz.setQuestions(questions);

        return quiz;
    }

    @Transactional
    public Quiz updateFullQuiz(Quiz updateQuizRequest) {
        Quiz quiz = quizRepository.findById(updateQuizRequest.getId())
                .orElseThrow(() -> new BadRequestException(ResponseMessage.INVALID_QUIZ_ID));
        quiz.setTitle(updateQuizRequest.getTitle());

        Map<Long, QuizQuestion> existingQuestionsMap = quiz.getQuestions().stream()
                .collect(Collectors.toMap(QuizQuestion::getId, Function.identity()));
        for (QuizQuestion updatedQuizQuestion : updateQuizRequest.getQuestions()) {
            QuizQuestion existingQuestion = existingQuestionsMap.get(updatedQuizQuestion.getId());
            if (existingQuestion != null) {
                existingQuestion.setTitle(updatedQuizQuestion.getTitle());

                Map<Long, QuizAnswer> existingAnswersMap = existingQuestion.getAnswers().stream()
                        .collect(Collectors.toMap(QuizAnswer::getId, Function.identity()));
                for (QuizAnswer newAnswerData : updatedQuizQuestion.getAnswers()) {
                    QuizAnswer existingAnswer = existingAnswersMap.get(newAnswerData.getId());
                    if (existingAnswer != null) {
                        existingAnswer.setTitle(newAnswerData.getTitle());
                        existingAnswer.setCorrect(newAnswerData.isCorrect());
                    } else {
                        throw new BadRequestException(ResponseMessage.INVALID_ANSWER_ID);
                    }
                }
            } else {
                throw new BadRequestException(ResponseMessage.INVALID_QUESTION_ID);
            }
        }

        return quiz;
    }

    public List<Quiz> getQuizzesByVideoId(Long videoId) {
        CourseFile courseFile = courseFileRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_VIDEO_ID));

        enrolledCourseService.validateCourseMaterialAccess(courseFile.getCourseTopic().getCourse());

        return quizRepository.findByVideoOrderByIdAsc(courseFile);
    }

    public Quiz getQuiz(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));
    }

    public Quiz updateQuiz(Long quizId, UpdateQuizDto updateQuizDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));

        if(!StringUtils.isEmpty(updateQuizDto.getTitle()) && !updateQuizDto.getTitle().equals(quiz.getTitle())) {
            quiz.setTitle(updateQuizDto.getTitle());
        }
        if(updateQuizDto.getQuestionMark() != 0 && updateQuizDto.getQuestionMark() != quiz.getQuestionMark()) {
            quiz.setQuestionMark(updateQuizDto.getQuestionMark());
        }
        if(updateQuizDto.getDuration() != 0 && updateQuizDto.getDuration() != quiz.getDuration()) {
            quiz.setDuration(updateQuizDto.getDuration());
        }

        return quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_QUIZ_ID));

        quizRepository.delete(quiz);
    }

    private CourseFile validateQuizCourseFile(Long videoId) {
        CourseFile courseFile = courseFileRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_VIDEO_ID));

        if(!courseFile.getType().equals(CourseFileType.VIDEO)) {
            throw new BadRequestException(ResponseMessage.INVALID_VIDEO_ID);
        }

        return courseFile;
    }

    private void validateCorrectQuizAnswers(List<CreateFullQuizQuestionDto> questions) {
        for(CreateFullQuizQuestionDto question : questions) {
            if (question.getAnswers().stream()
                    .filter(CreateFullQuizAnswerDto::isCorrect)
                    .count() > 1) {
                throw new BadRequestException(ResponseMessage.MULTIPLE_CORRECT_ANSWERS + question.getTitle());
            }
        }
    }
}
