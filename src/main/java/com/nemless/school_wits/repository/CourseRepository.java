package com.nemless.school_wits.repository;

import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByGrade(Grade grade);

    Optional<Course> findByUid(String uid);

    List<Course> findAllByGradeAndMode(Grade grade, CourseMode mode);
}
