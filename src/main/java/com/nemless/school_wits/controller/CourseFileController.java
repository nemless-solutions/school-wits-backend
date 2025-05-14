package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.UploadCourseFileDto;
import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.service.CourseFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/course_file")
public class CourseFileController {
    private final CourseFileService courseFileService;

    @GetMapping("/{courseId}")
    ResponseEntity<List<CourseFile>> getCourseFileList(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseFileService.getCourseFileList(courseId));
    }

    @PostMapping
    ResponseEntity<CourseFile> saveCourseFile(UploadCourseFileDto uploadCourseFileDto) {
        log.info("Uploading new file: {}", uploadCourseFileDto);

        return ResponseEntity.ok(courseFileService.saveFile(uploadCourseFileDto));
    }
}
