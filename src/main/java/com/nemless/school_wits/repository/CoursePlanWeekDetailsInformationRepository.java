package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.CoursePlanWeekDetailsInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePlanWeekDetailsInformationRepository extends JpaRepository<CoursePlanWeekDetailsInformation, Long> {
}
