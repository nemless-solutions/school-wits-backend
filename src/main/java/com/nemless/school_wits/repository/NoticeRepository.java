package com.nemless.school_wits.repository;

import com.nemless.school_wits.model.Notice;
import com.nemless.school_wits.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByRecipientsOrderByCreatedAtDesc(User user);

    List<Notice> findAllByOrderByCreatedAtDesc();
}
