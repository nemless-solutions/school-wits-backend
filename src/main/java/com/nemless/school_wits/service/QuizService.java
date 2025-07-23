package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizDto;
import com.nemless.school_wits.dto.request.UpdateQuizDto;
import com.nemless.school_wits.enums.CourseFileType;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.repository.CourseFileRepository;
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
public class QuizService {
    private final QuizRepository quizRepository;
    private final CourseFileRepository courseFileRepository;
    private final EnrolledCourseService enrolledCourseService;

    @Transactional
    public Quiz createQuiz(CreateQuizDto createQuizDto) {
        CourseFile courseFile = courseFileRepository.findById(createQuizDto.getVideoId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_VIDEO_ID));

        if(!courseFile.getType().equals(CourseFileType.VIDEO)) {
            throw new BadRequestException(ResponseMessage.INVALID_VIDEO_ID);
        }

        Quiz quiz = Quiz.builder()
                .video(courseFile)
                .title(createQuizDto.getTitle())
                .questionMark(createQuizDto.getQuestionMark())
                .duration(createQuizDto.getDuration())
                .build();

        return quizRepository.save(quiz);
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
}
