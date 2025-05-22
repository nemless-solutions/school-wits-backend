package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.EnrolledCourse;
import com.nemless.school_wits.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long> {
    boolean existsByUserAndCourse(User user, Course course);

    Optional<EnrolledCourse> findByUserAndCourse(User user, Course course);
}
