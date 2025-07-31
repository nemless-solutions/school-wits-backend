package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateNoticeDto;
import com.nemless.school_wits.dto.request.UpdateNoticeDto;
import com.nemless.school_wits.enums.Role;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Notice;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.NoticeRepository;
import com.nemless.school_wits.repository.UserRepository;
import com.nemless.school_wits.util.AuthUtils;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public void createNotice(CreateNoticeDto createNoticeDto) {
        Notice notice = new Notice();
        notice.setTitle(createNoticeDto.getTitle());
        notice.setDetails(createNoticeDto.getDetails());

        if(createNoticeDto.isNotifyAll()) {
            List<User> students = userRepository.findByRoles_Name(Role.ROLE_STUDENT);
            notice.getRecipients().addAll(students);
        } else if(createNoticeDto.getGrade() != null) {
            List<User> students = userRepository.findByGrade(createNoticeDto.getGrade());
            notice.getRecipients().addAll(students);
        } else if(createNoticeDto.getUserId() != null) {
            User user = userRepository.findById(createNoticeDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_USER_ID));
            notice.getRecipients().add(user);
        } else if(createNoticeDto.getCourseId() != null) {
            List<User> students = userRepository.findByEnrollments_Course_Id(createNoticeDto.getCourseId());
            notice.getRecipients().addAll(students);
        } else {
            throw new BadRequestException(ResponseMessage.INVALID_NOTICE_RECIPIENT);
        }

        noticeRepository.save(notice);
    }

    public Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_NOTICE_ID));
    }

    public void updateNotice(Long noticeId, UpdateNoticeDto updateNoticeDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_NOTICE_ID));

        if(!StringUtils.isEmpty(updateNoticeDto.getTitle()) && !updateNoticeDto.getTitle().equals(notice.getTitle())) {
            notice.setTitle(updateNoticeDto.getTitle());
        }
        if(!StringUtils.isEmpty(updateNoticeDto.getDetails()) && !updateNoticeDto.getDetails().equals(notice.getDetails())) {
            notice.setDetails(updateNoticeDto.getDetails());
        }

        noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_NOTICE_ID));

        notice.getRecipients().clear();
        noticeRepository.save(notice);
        noticeRepository.flush();

        noticeRepository.delete(notice);
    }

    public List<Notice> getUserNotices() {
        return noticeRepository.findByRecipientsOrderByCreatedAtDesc(authUtils.getAuthenticatedUser());
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }
}
