package com.nemless.school_wits.service;

import com.nemless.school_wits.dto.request.CreateCourseRequestDto;
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

    public Course createCourse(CreateCourseRequestDto createCourseRequestDto) {
        Course course = new Course();
        course.setTitle(createCourseRequestDto.getTitle());
        course.setDescription(createCourseRequestDto.getDescription());
        course.setUid(createCourseRequestDto.getUid());
        course.setFee(createCourseRequestDto.getFee());

        return courseRepository.save(course);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }
}
