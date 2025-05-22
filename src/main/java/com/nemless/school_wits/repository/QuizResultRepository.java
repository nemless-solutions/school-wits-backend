package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.Quiz;
import com.nemless.school_wits.model.QuizResult;
import com.nemless.school_wits.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUser(User user);

    boolean existsByUserAndQuiz(User user, Quiz quiz);
}
