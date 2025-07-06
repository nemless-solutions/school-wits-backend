package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.AttendanceDto;
import com.nemless.school_wits.model.Attendance;
import com.nemless.school_wits.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Attendance> createAttendance(@Valid @RequestBody AttendanceDto attendanceDto) {
        log.info("Saving attendance: {}", attendanceDto);

        return ResponseEntity.ok(attendanceService.createAttendance(attendanceDto));
    }
}
