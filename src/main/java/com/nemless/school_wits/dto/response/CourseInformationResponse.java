package com.nemless.school_wits.dto.response;

import com.nemless.school_wits.model.CoursePlanInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
public class CourseInformationResponse {
    private Long id;
    private String title;
    private String description;
    private String learningContentTitle;
    private String learningContentList;
    private Map<String, Integer> chartValues;
    private String assessment;
    private String academicPlan;
    private Map<String, List<String>> coreLearningAreas;
    private CoursePlanInformation coursePlanInformation;
}
