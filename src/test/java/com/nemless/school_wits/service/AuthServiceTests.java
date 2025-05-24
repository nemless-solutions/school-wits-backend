package com.nemless.school_wits.service;

import com.nemless.school_wits.dto.request.UserLoginDto;
import com.nemless.school_wits.dto.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AuthServiceTests {
    @Autowired
    private AuthService authService;

    @Test
    void shouldLogin() {
        String email = "admin@schoolwits.com";
        String password = "admin";
        UserLoginDto userLoginDto = new UserLoginDto(email, password);

        AuthResponse result = authService.login(userLoginDto);

        assertThat(result.getToken()).isNotNull();
        assertThat(result.getUser().getEmail()).isEqualTo(email);
    }
}
