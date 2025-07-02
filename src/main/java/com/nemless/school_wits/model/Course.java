package com.nemless.school_wits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.CourseType;
import com.nemless.school_wits.enums.Grade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseType type;

    @Column(nullable = false)
    private float fee;

    private int numberOfLessons;
    private int numberOfNotes;
    private int numberOfWorksheet;
    private int numberOfQuizzes;
    private int numberOfExams;
    private int numberOfSession;
    private String academicSession;
    private String sessionDuration;

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CourseTopic> topics = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EnrolledCourse> enrollments = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private List<CourseBundle> bundles = new ArrayList<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedAt;
}
