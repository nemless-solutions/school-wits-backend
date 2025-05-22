package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateQuizDto;
import com.nemless.school_wits.enums.CourseFileType;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.repository.CourseFileRepository;
import com.nemless.school_wits.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final CourseFileRepository courseFileRepository;

    @Transactional
    public Quiz createQuiz(CreateQuizDto createQuizDto) {
        CourseFile courseFile = courseFileRepository.findById(createQuizDto.getVideoId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_VIDEO_ID));

        if(!courseFile.getType().equals(CourseFileType.VIDEO)) {
            throw new BadRequestException(ResponseMessage.INVALID_VIDEO_ID);
        }

        Quiz quiz = new Quiz();
        quiz.setVideo(courseFile);
        quiz.setQuestionMark(createQuizDto.getQuestionMark());
        quiz.setDuration(createQuizDto.getDuration());
        quiz = quizRepository.save(quiz);
        courseFile.setQuiz(quiz);
        courseFileRepository.save(courseFile);

        return quiz;
    }

    public Quiz getQuizByVideoId(Long videoId) {
        CourseFile courseFile = courseFileRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_VIDEO_ID));

        return quizRepository.findByVideo(courseFile)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.QUIZ_NOT_FOUND));
    }
}
