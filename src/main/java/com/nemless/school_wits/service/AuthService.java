package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.config.UserDetailsImpl;
import com.nemless.school_wits.dto.request.UserLoginDto;
import com.nemless.school_wits.dto.request.UserRegistrationDto;
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

    public AuthResponse registerUser(UserRegistrationDto userRegistrationDto) {
        if(userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new BadRequestException(ResponseMessage.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .fullName(userRegistrationDto.getFullName())
                .contact(userRegistrationDto.getContact())
                .uid(generateUid())
                .fatherName(userRegistrationDto.getFatherName())
                .motherName(userRegistrationDto.getMotherName())
                .guardianEmail(userRegistrationDto.getGuardianEmail())
                .guardianContact(userRegistrationDto.getGuardianContact())
                .curriculum(userRegistrationDto.getCurriculum())
                .grade(userRegistrationDto.getGrade())
                .dateOfBirth(userRegistrationDto.getDateOfBirth())
                .build();

        List<Role> roles = roleRepository.findByNameIn(
                List.of(com.nemless.school_wits.enums.Role.ROLE_STUDENT)
        );
        user.setRoles(roles);

        userRepository.save(user);
        log.info("New user created: {}", user);

        String token = getToken(userRegistrationDto.getEmail(), userRegistrationDto.getPassword());
        return new AuthResponse(user, token);
    }

    public AuthResponse login(UserLoginDto userLoginDto) {
        if(!userRepository.existsByEmail(userLoginDto.getEmail())) {
            throw new BadRequestException(ResponseMessage.INCORRECT_CREDENTIALS);
        }

        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new BadRequestException(ResponseMessage.INCORRECT_CREDENTIALS));

        String token = getToken(user.getEmail(), userLoginDto.getPassword());
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
