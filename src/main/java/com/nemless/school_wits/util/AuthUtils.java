package com.nemless.school_wits.util;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.config.UserDetailsImpl;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthUtils {
    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findById(userDetails.getId());
            return userOptional.orElse(null);
        } else {
            throw new BadRequestException(ResponseMessage.INVALID_TOKEN);
        }
    }
}