package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.ConfirmPaymentDto;
import com.nemless.school_wits.dto.request.InitiatePaymentDto;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.exception.UnauthorizedException;
import com.nemless.school_wits.model.EnrolledCourse;
import com.nemless.school_wits.model.Payment;
import com.nemless.school_wits.repository.EnrolledCourseRepository;
import com.nemless.school_wits.repository.PaymentRepository;
import com.nemless.school_wits.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final AuthUtils authUtils;

    public List<Payment> getAllPayment() {
        return paymentRepository.findAll();
    }

    @Transactional
    public void initiatePayment(InitiatePaymentDto initiatePaymentDto) {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findById(initiatePaymentDto.getEnrolledCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_ENROLLED_COURSE_ID));
        if(enrolledCourse.getPayment() != null) {
            throw new BadRequestException(ResponseMessage.PAYMENT_ALREADY_EXISTS);
        } else if (!enrolledCourse.getUser().equals(authUtils.getAuthenticatedUser())) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED_REQUEST);
        }

        Payment payment = new Payment();
        payment.setPaidFrom(initiatePaymentDto.getPaidFrom());
        payment.setTransactionId(initiatePaymentDto.getTransactionId());
        payment.setEnrolledCourse(enrolledCourse);
        payment = paymentRepository.save(payment);

        enrolledCourse.setPayment(payment);
        enrolledCourseRepository.save(enrolledCourse);
    }

    public void confirmPayment(ConfirmPaymentDto confirmPaymentDto) {
        Payment payment = paymentRepository.findById(confirmPaymentDto.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_PAYMENT_ID));

        float paidAmount = confirmPaymentDto.getPaidAmount();
        if(paidAmount < 0 || paidAmount > payment.getEnrolledCourse().getCourse().getFee()) {
            throw new BadRequestException(ResponseMessage.INVALID_AMOUNT);
        }

        payment.setPaidAmount(paidAmount);
        payment.setPaid(confirmPaymentDto.isPaid());
        paymentRepository.save(payment);
    }
}
