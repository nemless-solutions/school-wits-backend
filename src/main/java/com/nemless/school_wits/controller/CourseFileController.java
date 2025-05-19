package com.nemless.school_wits.controller;

import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.service.CourseFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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

    @GetMapping("/{courseTopicId}")
    ResponseEntity<List<CourseFile>> getCourseFileList(@PathVariable Long courseTopicId) {
        return ResponseEntity.ok(courseFileService.getCourseFileList(courseTopicId));
    }

    @GetMapping("/download/{fileId}")
    ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        return courseFileService.downloadFile(fileId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CourseFile> saveCourseFile(
            @RequestParam("courseTopicId") Long courseTopicId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestPart("file") MultipartFile file
    ) {
        log.info("Uploading new file in course topic: {}", courseTopicId);

        return ResponseEntity.ok(courseFileService.saveFile(courseTopicId, title, description, file));
    }
}
