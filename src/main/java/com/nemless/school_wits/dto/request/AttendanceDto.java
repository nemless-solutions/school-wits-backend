package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@AllArgsConstructor
public class AttendanceDto {
    @NotNull(message = "Enrolled course id is required")
    private Long enrolledCourseId;

    @NotNull(message = "Attendance is required")
    private boolean isPresent;

    @NotNull(message = "Date is required")
    private Date date;

    private boolean isLate;
}
