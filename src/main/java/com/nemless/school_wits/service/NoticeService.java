package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateNoticeDto;
import com.nemless.school_wits.enums.Role;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Notice;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.NoticeRepository;
import com.nemless.school_wits.repository.UserRepository;
import com.nemless.school_wits.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            for(User user : userRepository.findByRoles_Name(Role.ROLE_STUDENT)) {
                notice.getRecipients().add(user);
            }
        } else if(createNoticeDto.getGrade() != null) {
            for(User user : userRepository.findByGrade(createNoticeDto.getGrade())) {
                notice.getRecipients().add(user);
            }
        } else if(createNoticeDto.getUserId() != null) {
            User user = userRepository.findById(createNoticeDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_USER_ID));
            notice.getRecipients().add(user);
        } else {
            throw new BadRequestException(ResponseMessage.INVALID_NOTICE_RECIPIENT);
        }

        noticeRepository.save(notice);
    }

    public List<Notice> getUserNotices() {
        return noticeRepository.findByRecipientsOrderByCreatedAtDesc(authUtils.getAuthenticatedUser());
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }
}
