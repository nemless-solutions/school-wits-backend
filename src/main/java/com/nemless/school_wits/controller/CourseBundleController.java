package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UpdateBundleFeesDto;
import com.nemless.school_wits.model.CourseBundle;
import com.nemless.school_wits.service.CourseBundleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ResponseEntity<List<CourseBundle>> getCourseBundlesByGrade(@PathVariable String gradeName) {
        return ResponseEntity.ok(courseBundleService.findCourseBundlesByGrade(gradeName));
    }

    @PutMapping("/{bundleId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> updateBundleFees(@PathVariable Long bundleId, @RequestBody UpdateBundleFeesDto updateBundleFeesDto) {
        log.info("Updating bundle fees {}: {}", bundleId, updateBundleFeesDto);

        courseBundleService.updateBundleFees(bundleId, updateBundleFeesDto);

        return ResponseEntity.ok(ResponseMessage.BUNDLE_FEE_UPDATED);
    }
}
