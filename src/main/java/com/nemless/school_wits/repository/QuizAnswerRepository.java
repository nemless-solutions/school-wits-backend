package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.QuizAnswer;
import com.nemless.school_wits.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    Optional<QuizAnswer> findByQuestionAndIsCorrect(QuizQuestion question, boolean isCorrect);
}
