package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.CreateCourseTopicDto;
import com.nemless.school_wits.model.CourseTopic;
import com.nemless.school_wits.service.CourseTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/course_topic")
public class CourseTopicController {
    private final CourseTopicService courseTopicService;

    @GetMapping("/course/{courseId}")
    ResponseEntity<List<CourseTopic>> getAllCourseTopics(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseTopicService.getAllCourseTopics(courseId));
    }

    @PostMapping
    ResponseEntity<CourseTopic> createCourseTopic(@Valid @RequestBody CreateCourseTopicDto createCourseTopicDto) {
        log.info("Creating new course topic: {}", createCourseTopicDto);

        return ResponseEntity.ok(courseTopicService.createCourseTopic(createCourseTopicDto));
    }

    @GetMapping("/{courseTopicId}")
    ResponseEntity<CourseTopic> getCourseTopicById(@PathVariable Long courseTopicId) {
        return ResponseEntity.ok(courseTopicService.getCourseTopicById(courseTopicId));
    }
}
