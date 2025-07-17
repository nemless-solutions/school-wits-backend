package com.nemless.school_wits.repository;

import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.model.CourseBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseBundleRepository extends JpaRepository<CourseBundle, Long> {
    List<CourseBundle> findByGrade(Grade grade);
}
