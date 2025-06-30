package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.CoursePlanWeekInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePlanWeekInformationRepository extends JpaRepository<CoursePlanWeekInformation, Long> {
}
