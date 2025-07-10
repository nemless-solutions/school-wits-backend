package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.CourseFile;
import com.nemless.school_wits.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByVideo(CourseFile video);
}
