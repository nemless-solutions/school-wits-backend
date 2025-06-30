package com.nemless.school_wits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_plan_information")
public class CoursePlanInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_information_id", nullable = false)
    @JsonIgnore
    private CourseInformation courseInformation;

    @Builder.Default
    @OneToMany(mappedBy = "coursePlanInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursePlanWeekInformation> weeks = new ArrayList<>();
}
