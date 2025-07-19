package com.nemless.school_wits.controller;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.ChangePasswordDto;
import com.nemless.school_wits.dto.request.UserLoginDto;
import com.nemless.school_wits.dto.request.UserRegistrationDto;
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
    ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        log.info("Registration request: {}", userRegistrationDto);

        return ResponseEntity.ok(authService.registerUser(userRegistrationDto));
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        log.info("Login request: {}", userLoginDto);

        return ResponseEntity.ok(authService.login(userLoginDto));
    }

    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        log.info("Change password request: {}", changePasswordDto);

        authService.changePassword(changePasswordDto);

        return ResponseEntity.ok(ResponseMessage.PASSWORD_CHANGE_SUCCESSFUL);
    }
}
