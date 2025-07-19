package com.nemless.school_wits.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ChangePasswordDto {
    @NotBlank(message = "Current password is required")
    @Size(min = 5, message = "Current password must be at least 5 characters long")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 5, message = "New password must be at least 5 characters long")
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    @Size(min = 5, message = "Confirm new password must be at least 5 characters long")
    private String confirmNewPassword;
}
