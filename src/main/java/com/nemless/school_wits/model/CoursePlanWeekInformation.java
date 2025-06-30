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
@Table(name = "course_plan_week_information")
public class CoursePlanWeekInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_plan_information_id", nullable = false)
    @JsonIgnore
    private CoursePlanInformation coursePlanInformation;

    @Column(nullable = false)
    private String text;

    @Builder.Default
    @OneToMany(mappedBy = "coursePlanWeekInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursePlanWeekDetailsInformation> weekDetails = new ArrayList<>();
}
