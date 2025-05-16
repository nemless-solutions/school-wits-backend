package com.nemless.school_wits.dto.response;

import com.nemless.school_wits.model.EnrolledCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnrolledCourseDto {
    private EnrolledCourse enrolledCourse;
    private Long paymentId;
    private boolean isPaid;
}
