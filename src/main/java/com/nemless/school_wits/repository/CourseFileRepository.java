package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.CourseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseFileRepository extends JpaRepository<CourseFile, Long> {
}
