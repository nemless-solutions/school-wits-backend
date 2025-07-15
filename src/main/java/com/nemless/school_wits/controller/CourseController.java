package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateCourseDto;
import com.nemless.school_wits.dto.request.UpdateCourseDto;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Course> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
        log.info("Creating new course: {}", createCourseDto);

        return ResponseEntity.ok(courseService.createCourse(createCourseDto));
    }

    @GetMapping("/{courseId}")
    ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}")
    ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @RequestBody UpdateCourseDto updateCourseDto) {
        log.info("Updating course {}: {}", courseId, updateCourseDto);

        return ResponseEntity.ok(courseService.updateCourse(courseId, updateCourseDto));
    }

    @GetMapping("/grade/{name}")
    ResponseEntity<List<Course>> getCoursesByGrade(@PathVariable String name, @RequestParam(required = false) String mode) {
        return ResponseEntity.ok(courseService.getCoursesByGrade(name, mode));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}")
    ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        log.info("Deleting course: {}", courseId);

        courseService.deleteCourse(courseId);

        return ResponseEntity.ok(ResponseMessage.COURSE_DELETE_SUCCESSFUL);
    }
}
