package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ConfirmPaymentDto {
    @NotNull(message = "Payment id is required")
    private Long paymentId;

    @NotNull(message = "Paid amount is required")
    private float paidAmount;

    @NotNull(message = "Payment status is required")
    private boolean isPaid;
}
