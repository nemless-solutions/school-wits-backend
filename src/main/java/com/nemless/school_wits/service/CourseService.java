package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateCourseDto;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByGrade(String gradeName) {
        try {
            return courseRepository.findAllByGrade(Grade.valueOf(gradeName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid grade: " + gradeName);
        }
    }

    public Course createCourse(CreateCourseDto createCourseDto) {
        if(createCourseDto.getFee() < 0) {
            throw new BadRequestException(ResponseMessage.INVALID_AMOUNT);
        }

        Course course = Course.builder()
                .grade(createCourseDto.getGrade())
                .mode(createCourseDto.getMode())
                .type(createCourseDto.getType())
                .title(createCourseDto.getTitle())
                .description(createCourseDto.getDescription())
                .uid(createCourseDto.getUid())
                .fee(createCourseDto.getFee())
                .build();

        return courseRepository.save(course);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));
    }
}
