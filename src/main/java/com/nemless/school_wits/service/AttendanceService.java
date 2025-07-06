package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.AttendanceDto;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Attendance;
import com.nemless.school_wits.model.EnrolledCourse;
import com.nemless.school_wits.repository.AttendanceRepository;
import com.nemless.school_wits.repository.EnrolledCourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;

    public Attendance createAttendance(AttendanceDto attendanceDto) {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findById(attendanceDto.getEnrolledCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_ENROLLED_COURSE_ID));

        Attendance attendance = Attendance.builder()
                .enrolledCourse(enrolledCourse)
                .date(attendanceDto.getDate())
                .isPresent(attendanceDto.isPresent())
                .isLate(attendanceDto.isLate())
                .build();

        return attendanceRepository.save(attendance);
    }
}
