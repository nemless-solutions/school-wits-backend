package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.CourseInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseInformationRepository extends JpaRepository<CourseInformation, Long> {
    Optional<CourseInformation> findByCourse_Id(Long courseId);
}
