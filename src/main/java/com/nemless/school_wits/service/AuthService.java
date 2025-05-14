package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.config.UserDetailsImpl;
import com.nemless.school_wits.dto.request.LoginRequestDto;
import com.nemless.school_wits.dto.request.UserRegistrationRequestDto;
import com.nemless.school_wits.dto.response.AuthResponse;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.model.Role;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.RoleRepository;
import com.nemless.school_wits.repository.UserRepository;
import com.nemless.school_wits.util.AuthUtils;
import com.nemless.school_wits.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthUtils authUtils;

    private String generateUid() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public AuthResponse registerUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        if(userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new BadRequestException(ResponseMessage.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.setEmail(userRegistrationRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setFullName(userRegistrationRequestDto.getFullName());
        user.setContact(userRegistrationRequestDto.getContact());
        user.setUid(generateUid());
        List<Role> roles = roleRepository.findByNameIn(
                List.of(com.nemless.school_wits.enums.Role.ROLE_STUDENT)
        );
        user.setRoles(roles);

        userRepository.save(user);
        log.info("New user created: {}", user);

        String token = getToken(userRegistrationRequestDto.getEmail(), userRegistrationRequestDto.getPassword());
        return new AuthResponse(user, token);
    }

    public AuthResponse login(LoginRequestDto loginRequestDto) {
        if(!userRepository.existsByEmail(loginRequestDto.getEmail())) {
            throw new BadRequestException(ResponseMessage.INCORRECT_CREDENTIALS);
        }

        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElse(null);
        if(user == null) return null;

        String token = getToken(user.getEmail(), loginRequestDto.getPassword());
        return new AuthResponse(user, token);
    }

    public User getAuthenticatedUser() {
        return authUtils.getAuthenticatedUser();
    }

    private String getToken(String email, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            throw new BadRequestException(ResponseMessage.INCORRECT_CREDENTIALS);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return jwtUtils.generateTokenFromUserDetails(userDetails);
    }
}
