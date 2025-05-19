package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateCourseDto;
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

    public Course createCourse(CreateCourseDto createCourseDto) {
        if(createCourseDto.getFee() < 0) {
            throw new BadRequestException(ResponseMessage.INVALID_AMOUNT);
        }

        Course course = new Course();
        course.setGrade(createCourseDto.getGrade());
        course.setTitle(createCourseDto.getTitle());
        course.setDescription(createCourseDto.getDescription());
        course.setUid(createCourseDto.getUid());
        course.setFee(createCourseDto.getFee());

        return courseRepository.save(course);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));
    }
}
