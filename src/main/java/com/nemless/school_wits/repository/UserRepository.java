package com.nemless.school_wits.repository;

import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.enums.Role;
import com.nemless.school_wits.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRoles_Name(Role role);

    List<User> findByGrade(Grade grade);

    long countByRoles_Name(Role role);

    List<User> findByFullNameContainingIgnoreCase(String name);

    List<User> findByEnrollmentsIsEmpty();
}
