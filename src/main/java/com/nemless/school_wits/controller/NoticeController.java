package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.CreateNoticeDto;
import com.nemless.school_wits.dto.request.UpdateNoticeDto;
import com.nemless.school_wits.model.Notice;
import com.nemless.school_wits.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<String> createNotice(@Valid @RequestBody CreateNoticeDto createNoticeDto) {
        log.info("Creating notice: {}", createNoticeDto);
        noticeService.createNotice(createNoticeDto);
        return ResponseEntity.ok(ResponseMessage.NOTICE_POSTED);
    }

    @GetMapping
    ResponseEntity<List<Notice>> getUserNotices() {
        return ResponseEntity.ok(noticeService.getUserNotices());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{noticeId}")
    ResponseEntity<Notice> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{noticeId}")
    ResponseEntity<String> updateNotice(@PathVariable Long noticeId, @Valid @RequestBody UpdateNoticeDto updateNoticeDto) {
        log.info("Updating notice {}: {}", noticeId, updateNoticeDto);
        noticeService.updateNotice(noticeId, updateNoticeDto);
        return ResponseEntity.ok(ResponseMessage.NOTICE_UPDATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{noticeId}")
    ResponseEntity<String> deleteNotice(@PathVariable Long noticeId) {
        log.info("Deleting notice: {}", noticeId);
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(ResponseMessage.NOTICE_DELETED);
    }
}
