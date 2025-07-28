package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UpdateCourseInfographicsRequest;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseInformation;
import com.nemless.school_wits.repository.CourseInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Transactional
    public void updateInfographics(List<UpdateCourseInfographicsRequest> infographicsRequestList) {
        for(UpdateCourseInfographicsRequest infographicsRequest : infographicsRequestList) {
            List<CourseInformation> courseInformationList = new ArrayList<>();
            for(Map.Entry<Long, Map<String, Integer>> infographicsEntry : infographicsRequest.getInfographicsMap().entrySet()) {
                CourseInformation courseInformation = courseInformationRepository.findByCourse_Id(infographicsEntry.getKey())
                        .orElseThrow(() -> new BadRequestException(ResponseMessage.INVALID_INFOGRAPHICS_COURSE_ID));

                courseInformation.setChartValues(infographicsEntry.getValue());
                courseInformationList.add(courseInformation);
            }
            courseInformationRepository.saveAll(courseInformationList);
        }
    }
}
