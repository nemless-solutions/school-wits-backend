package com.nemless.school_wits.controller;

import com.nemless.school_wits.model.EnrolledCourse;
import com.nemless.school_wits.service.EnrolledCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/enrolled_course")
public class EnrolledCourseController {
    private final EnrolledCourseService enrolledCourseService;

    @GetMapping
    ResponseEntity<List<EnrolledCourse>> getEnrollments() {
        return ResponseEntity.ok(enrolledCourseService.getEnrollments());
    }

    @PostMapping
    ResponseEntity<EnrolledCourse> enrollInCourse(@RequestBody Long courseId) {
        return ResponseEntity.ok(enrolledCourseService.enrollInCourse(courseId));
    }
}
