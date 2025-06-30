package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.CoursePlanInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePlanInformationRepository extends JpaRepository<CoursePlanInformation, Long> {
}
