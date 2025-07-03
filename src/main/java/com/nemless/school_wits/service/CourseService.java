package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateCourseDto;
import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByGrade(String gradeName, String mode) {
        CourseMode courseMode = null;
        List<Course> courses;

        if(!Objects.equals(mode, "")) {
            try {
                courseMode = CourseMode.valueOf(mode);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid course mode: " + mode);
            }
        }
        try {
            courses = courseRepository.findAllByGrade(Grade.valueOf(gradeName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid grade: " + gradeName);
        }

        if(courseMode != null) {
            courses = filterCourseList(courses, courseMode);
        }

        return courses;
    }

    private List<Course> filterCourseList(List<Course> courses, CourseMode mode) {
        return courses.stream()
                .filter(course -> course.getMode() == mode)
                .collect(Collectors.toList());
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
