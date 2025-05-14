package com.nemless.school_wits.dto.response;

import com.nemless.school_wits.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResponse {
    private User user;
    private String token;
}
