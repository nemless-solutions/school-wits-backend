package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.response.EnrolledCourseDto;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.EnrolledCourse;
import com.nemless.school_wits.model.Payment;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.CourseRepository;
import com.nemless.school_wits.repository.EnrolledCourseRepository;
import com.nemless.school_wits.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrolledCourseService {
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final CourseRepository courseRepository;
    private final AuthUtils authUtils;

    @Transactional
    public void enrollInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if(course == null) {
            throw new ResourceNotFoundException(ResponseMessage.INVALID_COURSE_ID);
        }
        User user = authUtils.getAuthenticatedUser();
        if(enrolledCourseRepository.existsByUserAndCourse(user, course)) {
            throw new BadRequestException(ResponseMessage.ENROLLMENT_EXISTS);
        }

        EnrolledCourse enrolledCourse = new EnrolledCourse();
        enrolledCourse.setCourse(course);
        enrolledCourse.setUser(user);
        user.getEnrollments().add(enrolledCourse);
        course.getEnrollments().add(enrolledCourse);

        enrolledCourseRepository.save(enrolledCourse);
        log.info("{} enrolled in {}", user.getFullName(), course.getTitle());
    }

    public List<EnrolledCourseDto> getEnrollments() {
        List<EnrolledCourseDto> list = new ArrayList<>();
        for(EnrolledCourse enrolledCourse : authUtils.getAuthenticatedUser().getEnrollments()) {
            list.add(getDtoFromEnrolledCourse(enrolledCourse));
        }
        return list;
    }

    private EnrolledCourseDto getDtoFromEnrolledCourse(EnrolledCourse enrolledCourse) {
        Payment payment = enrolledCourse.getPayment();
        Long paymentId = payment == null ? null : payment.getId();
        boolean isPaid = payment != null && payment.isPaid();
        return new EnrolledCourseDto(enrolledCourse, paymentId, isPaid);
    }
}
