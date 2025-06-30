package com.nemless.school_wits.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseInformationDto {
    private String course;
    private String title;
    private String description;
    private String learningContentTitle;
    private List<String> learningContentList;
    private String assessment;
    private String academicPlan;
}
