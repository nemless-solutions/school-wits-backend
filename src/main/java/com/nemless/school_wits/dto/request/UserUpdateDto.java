package com.nemless.school_wits.dto.request;

import com.nemless.school_wits.enums.Curriculum;
import com.nemless.school_wits.enums.Grade;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@AllArgsConstructor
public class UserUpdateDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min = 6)
    private String fullName;

    @Size(min = 6)
    private String currentSchool;

    @NotBlank(message = "Father's name is required")
    @Size(min = 6)
    private String fatherName;

    @NotBlank(message = "Mother's name is required")
    @Size(min = 6)
    private String motherName;

    @NotBlank(message = "Guardian's email is required")
    @Email(message = "Invalid email format")
    private String guardianEmail;

    @NotBlank(message = "Guardian's contact number is required")
    @Size(min = 7, max = 15, message = "Invalid contact number")
    private String guardianContact;

    @NotNull(message = "Curriculum is required")
    private Curriculum curriculum;

    @NotNull(message = "Grade is required")
    private Grade grade;

    @Past
    private Date dateOfBirth;

    private Date lastSeenNotice;
}
