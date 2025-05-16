package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.CreateCourseDto;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping
    ResponseEntity<Course> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
        log.info("Creating new course: {}", createCourseDto);

        return ResponseEntity.ok(courseService.createCourse(createCourseDto));
    }

    @GetMapping("/{courseId}")
    ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }
}
