package com.nemless.school_wits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_plan_week_details_information")
public class CoursePlanWeekDetailsInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_plan_week_information_id", nullable = false)
    @JsonIgnore
    private CoursePlanWeekInformation coursePlanWeekInformation;

    @Column(nullable = false)
    private String text;
}
