package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateCourseDto;
import com.nemless.school_wits.dto.request.UpdateCourseDto;
import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.repository.CourseRepository;
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
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByGrade(String gradeName, String mode) {
        CourseMode courseMode = null;
        List<Course> courses;

        if(mode != null && !mode.isEmpty()) {
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

    public Course updateCourse(Long courseId, UpdateCourseDto updateCourseDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        if(updateCourseDto.getMode() != null && !updateCourseDto.getMode().equals(course.getMode())) {
            course.setMode(updateCourseDto.getMode());
        }
        if(updateCourseDto.getType() != null && !updateCourseDto.getType().equals(course.getType())) {
            course.setType(updateCourseDto.getType());
        }
        if(!StringUtils.isEmpty(updateCourseDto.getTitle()) && !updateCourseDto.getTitle().equals(course.getTitle())) {
            course.setTitle(updateCourseDto.getTitle());
        }
        if(!StringUtils.isEmpty(updateCourseDto.getDescription()) && !updateCourseDto.getDescription().equals(course.getDescription())) {
            course.setDescription(updateCourseDto.getDescription());
        }
        if(!StringUtils.isEmpty(updateCourseDto.getUid()) && !updateCourseDto.getUid().equals(course.getUid())) {
            course.setUid(updateCourseDto.getUid());
        }
        if(updateCourseDto.getFee() != 0 && updateCourseDto.getFee() != course.getFee()) {
            course.setFee(updateCourseDto.getFee());
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        courseRepository.delete(course);
    }
}
