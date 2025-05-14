package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.LoginRequestDto;
import com.nemless.school_wits.dto.request.UserRegistrationRequestDto;
import com.nemless.school_wits.dto.response.AuthResponse;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping
    ResponseEntity<User> getAuthenticatedUser() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    @PostMapping("/register")
    ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto) {
        log.info("Registration request: {}", userRegistrationRequestDto);

        return ResponseEntity.ok(authService.registerUser(userRegistrationRequestDto));
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        log.info("Login request: {}", loginRequestDto);

        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
}
