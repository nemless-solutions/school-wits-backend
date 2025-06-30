package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ContactRequestDto {
    @NotBlank(message = "Name is required")
    private String fullName;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotBlank(message = "Message is required")
    private String message;
}
