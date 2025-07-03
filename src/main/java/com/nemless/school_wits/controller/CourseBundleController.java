package com.nemless.school_wits.controller;

import com.nemless.school_wits.model.CourseBundle;
import com.nemless.school_wits.service.CourseBundleService;
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
@RequestMapping("/course_bundle")
public class CourseBundleController {
    private final CourseBundleService courseBundleService;

    @GetMapping("/{bundleId}")
    ResponseEntity<CourseBundle> getCourseBundle(@PathVariable Long bundleId) {
        return ResponseEntity.ok(courseBundleService.findCourseBundleById(bundleId));
    }

    @GetMapping("/grade/{gradeName}")
    ResponseEntity<CourseBundle> getCourseBundleByGrade(@PathVariable String gradeName) {
        return ResponseEntity.ok(courseBundleService.findCourseBundleByGrade(gradeName));
    }
}
