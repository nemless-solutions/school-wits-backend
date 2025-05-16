package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class InitiatePaymentDto {
    @NotNull(message = "Enrolled course id is required")
    private Long enrolledCourseId;

    @NotBlank(message = "Payment phone number is required")
    @Size(min = 7, max = 15, message = "Invalid phone number")
    private String paidFrom;

    @NotBlank(message = "Transaction id is required")
    private String transactionId;
}
