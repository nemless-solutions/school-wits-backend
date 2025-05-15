package com.nemless.school_wits.controller;

import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.service.CourseFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CourseFile> saveCourseFile(
            @RequestParam("courseId") Long courseId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestPart("file") MultipartFile file
    ) {
        log.info("Uploading new file in course: {}", courseId);

        return ResponseEntity.ok(courseFileService.saveFile(courseId, title, description, file));
    }
}
