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
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6)
    private String fullName;

    @Size(min = 6)
    private String currentSchool;

    @Size(min = 6)
    private String fatherName;

    @Size(min = 6)
    private String motherName;

    @Email(message = "Invalid email format")
    private String guardianEmail;

    @Size(min = 7, max = 15, message = "Invalid contact number")
    private String guardianContact;

    private Curriculum curriculum;

    private Grade grade;

    @Past
    private Date dateOfBirth;

    private Date lastSeenNotice;
}
