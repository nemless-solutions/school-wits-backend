package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UpdateCourseInfographicsRequest;
import com.nemless.school_wits.model.CourseInformation;
import com.nemless.school_wits.service.CourseInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/infographics")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> updateInfographics(@RequestBody List<UpdateCourseInfographicsRequest> infographicsRequestList) {
        log.info("Updating infographics: {}", infographicsRequestList);

        courseInformationService.updateInfographics(infographicsRequestList);

        return ResponseEntity.ok(ResponseMessage.INFOGRAPHICS_UPDATE_SUCCESSFUL);
    }
}
