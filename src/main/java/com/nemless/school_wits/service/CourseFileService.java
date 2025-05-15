package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.repository.CourseFileRepository;
import com.nemless.school_wits.repository.CourseRepository;
import com.nemless.school_wits.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseFileService {
    private final CourseFileRepository courseFileRepository;
    private final CourseRepository courseRepository;

    public List<CourseFile> getCourseFileList(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        return courseFileRepository.findAllByCourse(course);
    }

    public CourseFile saveFile(Long courseId, String title, String description, MultipartFile file) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        FileUtils.validateCourseFile(file);
        String uid = generateUid();
        FileUtils.uploadFile(file, course.getUid(), uid);

        CourseFile courseFile = new CourseFile();
        courseFile.setCourse(course);
        courseFile.setType(FileUtils.getFileType(file));
        courseFile.setTitle(title);
        courseFile.setDescription(description);
        courseFile.setFileName(file.getOriginalFilename());
        courseFile.setFileUid(uid);

        return courseFileRepository.save(courseFile);
    }

    private String generateUid() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
