package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseBundle;
import com.nemless.school_wits.repository.CourseBundleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseBundleService {
    private final CourseBundleRepository courseBundleRepository;

    public CourseBundle findCourseBundleByGrade(String gradeName) {
        try {
            return courseBundleRepository.findByGrade(Grade.valueOf(gradeName.toUpperCase()))
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_BUNDLE));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid grade: " + gradeName);
        }
    }

    public CourseBundle findCourseBundleById(Long bundleId) {
        return courseBundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_BUNDLE));
    }
}
