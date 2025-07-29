package com.nemless.school_wits.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_information")
public class CourseInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @Column(nullable = false)
    @Lob
    private String learningContentTitle;

    @Column(nullable = false)
    @Lob
    private String learningContentList;

    @ElementCollection
    @CollectionTable(name = "barchart_values", joinColumns = @JoinColumn(name = "course_information_id"))
    @MapKeyColumn(name = "topic_name")
    @Column(name = "topic_value")
    @Builder.Default
    private Map<String, Integer> chartValues = new HashMap<>();

    @Column(nullable = false)
    @Lob
    private String assessment;

    @Column(nullable = false)
    @Lob
    private String academicPlan;

    @Lob
    private String coreLearningAreas;

    @OneToOne(mappedBy = "courseInformation", cascade = CascadeType.ALL)
    private CoursePlanInformation coursePlanInformation;
}
