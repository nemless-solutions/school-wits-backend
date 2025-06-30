package com.nemless.school_wits.controller;

import com.nemless.school_wits.model.CourseInformation;
import com.nemless.school_wits.service.CourseInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/course_information")
public class CourseInformationController {
    private final CourseInformationService courseInformationService;

    @GetMapping("/{courseId}")
    ResponseEntity<CourseInformation> getCourseInformation(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseInformationService.getCourseInformationByCourseId(courseId));
    }
}
