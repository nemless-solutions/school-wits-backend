package com.nemless.school_wits.repository;

import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.model.CourseBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseBundleRepository extends JpaRepository<CourseBundle, Long> {
    Optional<CourseBundle> findByGrade(Grade grade);
}
