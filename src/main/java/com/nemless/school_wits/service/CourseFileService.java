package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UpdateCourseFileDto;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.model.CourseTopic;
import com.nemless.school_wits.repository.CourseFileRepository;
import com.nemless.school_wits.repository.CourseTopicRepository;
import com.nemless.school_wits.util.FileUtils;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseFileService {
    private final CourseFileRepository courseFileRepository;
    private final CourseTopicRepository courseTopicRepository;
    private final EnrolledCourseService enrolledCourseService;

    public List<CourseFile> getCourseFileList(Long courseTopicId) {
        CourseTopic courseTopic = courseTopicRepository.findById(courseTopicId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_TOPIC_ID));

        enrolledCourseService.validateCourseMaterialAccess(courseTopic.getCourse());

        return courseFileRepository.findAllByCourseTopicOrderByIdAsc(courseTopic);
    }

    public CourseFile saveFile(Long courseTopicId, String title, String description, MultipartFile file) {
        CourseTopic courseTopic = courseTopicRepository.findById(courseTopicId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_TOPIC_ID));

        FileUtils.validateCourseFile(file);
        String savedFileName = FileUtils.saveFile(file, courseTopic.getId().toString());

        CourseFile courseFile = CourseFile.builder()
                .courseTopic(courseTopic)
                .type(FileUtils.getFileType(file))
                .title(title)
                .description(description)
                .fileName(file.getOriginalFilename())
                .fileUid(savedFileName)
                .build();

        return courseFileRepository.save(courseFile);
    }

    public ResponseEntity<Resource> downloadFile(Long fileId) {
        CourseFile courseFile = courseFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_FILE_ID));

        enrolledCourseService.validateCourseMaterialAccess(courseFile.getCourseTopic().getCourse());

        String filePath = courseFile.getCourseTopic().getId().toString();
        String fileName = courseFile.getFileUid();
        return FileUtils.downloadFile(filePath, fileName);
    }

    public ResponseEntity<Resource> streamFile(Long fileId, String rangeHeader) {
        CourseFile courseFile = courseFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_FILE_ID));

        enrolledCourseService.validateCourseMaterialAccess(courseFile.getCourseTopic().getCourse());

        String filePath = courseFile.getCourseTopic().getId().toString();
        String fileName = courseFile.getFileUid();
        return FileUtils.streamFile(filePath, fileName, rangeHeader);
    }

    public CourseFile updateCourseFile(Long fileId, UpdateCourseFileDto updateCourseFileDto) {
        CourseFile courseFile = courseFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_FILE_ID));

        if(!StringUtils.isEmpty(updateCourseFileDto.getTitle()) && !updateCourseFileDto.getTitle().equals(courseFile.getTitle())) {
            courseFile.setTitle(updateCourseFileDto.getTitle());
        }
        if(!StringUtils.isEmpty(updateCourseFileDto.getDescription()) && !updateCourseFileDto.getDescription().equals(courseFile.getDescription())) {
            courseFile.setDescription(updateCourseFileDto.getDescription());
        }

        return courseFileRepository.save(courseFile);
    }

    @Transactional
    public void deleteCourseFile(Long fileId) {
        CourseFile courseFile = courseFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_FILE_ID));

        FileUtils.deleteFile(courseFile.getCourseTopic().getId().toString(), courseFile.getFileUid());

        courseFileRepository.delete(courseFile);
    }
}
