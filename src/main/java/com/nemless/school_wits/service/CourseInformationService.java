package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UpdateCoreLearningAreasDto;
import com.nemless.school_wits.dto.request.UpdateCourseInfographicsRequest;
import com.nemless.school_wits.dto.response.CourseInformationResponse;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.CourseInformation;
import com.nemless.school_wits.repository.CourseInformationRepository;
import com.nemless.school_wits.util.StringUtils;
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
    public CourseInformationResponse getCourseInformationByCourseId(Long courseId) {
        CourseInformation courseInformation = courseInformationRepository.findByCourse_Id(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID));

        return CourseInformationResponse.builder()
                .id(courseInformation.getId())
                .title(courseInformation.getTitle())
                .description(courseInformation.getDescription())
                .learningContentTitle(courseInformation.getLearningContentTitle())
                .learningContentList(courseInformation.getLearningContentList())
                .chartValues(courseInformation.getChartValues())
                .assessment(courseInformation.getAssessment())
                .academicPlan(courseInformation.getAcademicPlan())
                .coreLearningAreas(StringUtils.parseCoreLearningAreaContentString(courseInformation.getCoreLearningAreas()))
                .build();
    }

    @Transactional
    public void updateInfographics(UpdateCourseInfographicsRequest infographicsRequest) {
        List<CourseInformation> courseInformationList = new ArrayList<>();
        for(Map.Entry<Long, Map<String, Integer>> infographicsEntry : infographicsRequest.getInfographicsMap().entrySet()) {
            CourseInformation courseInformation = courseInformationRepository.findByCourse_Id(infographicsEntry.getKey())
                    .orElseThrow(() -> new BadRequestException(ResponseMessage.INVALID_INFOGRAPHICS_COURSE_ID));

            courseInformation.setChartValues(infographicsEntry.getValue());
            courseInformationList.add(courseInformation);
        }
        courseInformationRepository.saveAll(courseInformationList);
    }

    @Transactional
    public void updateCoreLearningAreas(List<UpdateCoreLearningAreasDto> updateCoreLearningAreasDtos) {
        List<CourseInformation> courseInformationList = new ArrayList<>();
        for(UpdateCoreLearningAreasDto updateCoreLearningAreasDto : updateCoreLearningAreasDtos) {
            CourseInformation courseInformation = courseInformationRepository.findByCourse_Id(updateCoreLearningAreasDto.getCourseId())
                    .orElseThrow(() -> new BadRequestException(ResponseMessage.INVALID_INFOGRAPHICS_COURSE_ID));

            courseInformation.setCoreLearningAreas(updateCoreLearningAreasDto.getContent());
            courseInformationList.add(courseInformation);
        }
        courseInformationRepository.saveAll(courseInformationList);
    }
}
