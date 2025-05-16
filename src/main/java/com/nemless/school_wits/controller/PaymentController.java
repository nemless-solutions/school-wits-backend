package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.ConfirmPaymentDto;
import com.nemless.school_wits.dto.request.InitiatePaymentDto;
import com.nemless.school_wits.model.Payment;
import com.nemless.school_wits.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    ResponseEntity<List<Payment>> getAllPayment() {
        return ResponseEntity.ok(paymentService.getAllPayment());
    }

    @PostMapping
    ResponseEntity<String> initiatePayment(@Valid @RequestBody InitiatePaymentDto initiatePaymentDto) {
        log.info("Initiating payment: {}", initiatePaymentDto);
        paymentService.initiatePayment(initiatePaymentDto);
        return ResponseEntity.ok(ResponseMessage.INITIATE_PAYMENT_SUCCESS);
    }

    @PutMapping("/confirm")
    ResponseEntity<String> confirmPayment(@Valid @RequestBody ConfirmPaymentDto confirmPaymentDto) {
        log.info("Confirming payment: {}", confirmPaymentDto);
        paymentService.confirmPayment(confirmPaymentDto);
        return ResponseEntity.ok(ResponseMessage.PAYMENT_STATUS_UPDATED);
    }
}
