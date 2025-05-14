package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameIn(List<com.nemless.school_wits.enums.Role> roleNames);
}
