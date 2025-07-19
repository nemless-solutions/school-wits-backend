package com.nemless.school_wits.model;

import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.Grade;
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
@Table(name = "course_bundle")
public class CourseBundle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseMode mode;

    private float fee;

    private float discountedFee;

    @ManyToMany
    @JoinTable(
            name = "course_bundle_course",
            joinColumns = @JoinColumn(name = "course_bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();
}
