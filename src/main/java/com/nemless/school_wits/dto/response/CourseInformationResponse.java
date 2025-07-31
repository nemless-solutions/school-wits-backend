package com.nemless.school_wits.dto.response;

import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.CoursePlanInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class CourseInformationResponse {
    private Long id;
    private Course course;
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
