package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UploadCourseFileDto;
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

    public CourseFile saveFile(UploadCourseFileDto uploadCourseFileDto) {
        Course course = courseRepository.findById(uploadCourseFileDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        MultipartFile file = uploadCourseFileDto.getFile();
        FileUtils.validateCourseFile(file);

        // upload and get file link
        String fileLink = "fileLink";

        CourseFile courseFile = new CourseFile();
        courseFile.setCourse(course);
        courseFile.setType(FileUtils.getFileType(file));
        courseFile.setTitle(uploadCourseFileDto.getTitle());
        courseFile.setDescription(uploadCourseFileDto.getDescription());
        courseFile.setFileName(file.getOriginalFilename());
        courseFile.setFileLink(fileLink);

        return courseFileRepository.save(courseFile);
    }
}
