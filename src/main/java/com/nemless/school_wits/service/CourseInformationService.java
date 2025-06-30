package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseInformation;
import com.nemless.school_wits.repository.CourseInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseInformationService {
    private final CourseInformationRepository courseInformationRepository;

    @Transactional
    public CourseInformation getCourseInformationByCourseId(Long courseId) {
        return courseInformationRepository.findByCourse_Id(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));
    }
}
