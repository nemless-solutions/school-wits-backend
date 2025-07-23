package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.CourseTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTopicRepository extends JpaRepository<CourseTopic, Long> {
    List<CourseTopic> findAllByCourseOrderByIdAsc(Course course);
}
