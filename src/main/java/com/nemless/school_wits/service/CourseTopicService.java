package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateCourseTopicDto;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.CourseTopic;
import com.nemless.school_wits.repository.CourseRepository;
import com.nemless.school_wits.repository.CourseTopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseTopicService {
    private final CourseTopicRepository courseTopicRepository;
    private final CourseRepository courseRepository;

    public List<CourseTopic> getAllCourseTopics(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        return courseTopicRepository.findAllByCourse(course);
    }

    public CourseTopic createCourseTopic(CreateCourseTopicDto createCourseTopicDto) {
        Course course = courseRepository.findById(createCourseTopicDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        CourseTopic courseTopic = new CourseTopic();
        courseTopic.setTitle(createCourseTopicDto.getTitle());
        courseTopic.setDescription(createCourseTopicDto.getDescription());
        courseTopic.setCourse(course);
        courseTopic.setLocked(createCourseTopicDto.isLocked());
        courseTopic = courseTopicRepository.save(courseTopic);

        course.getTopics().add(courseTopic);
        courseRepository.save(course);
        return courseTopic;
    }

    public CourseTopic getCourseTopicById(Long courseTopicId) {
        return courseTopicRepository.findById(courseTopicId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_TOPIC_ID));
    }
}
