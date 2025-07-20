package com.nemless.school_wits.service;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.dto.request.UserUpdateDto;
import com.nemless.school_wits.dto.response.DataSummary;
import com.nemless.school_wits.enums.Role;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.CourseFileRepository;
import com.nemless.school_wits.repository.UserRepository;
import com.nemless.school_wits.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final CourseFileRepository courseFileRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_USER_ID));
    }

    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_USER_ID));

        if (!StringUtils.isEmpty(userUpdateDto.getEmail())) {
            if (!userUpdateDto.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(userUpdateDto.getEmail())) {
                throw new BadRequestException(ResponseMessage.EMAIL_EXISTS);
            }
            user.setEmail(userUpdateDto.getEmail());
        }

        if(!StringUtils.isEmpty(userUpdateDto.getFullName()) && !userUpdateDto.getFullName().equals(user.getFullName())) {
            user.setFullName(userUpdateDto.getFullName());
        }
        if(!StringUtils.isEmpty(userUpdateDto.getCurrentSchool()) && !userUpdateDto.getCurrentSchool().equals(user.getCurrentSchool())) {
            user.setCurrentSchool(userUpdateDto.getCurrentSchool());
        }
        if(!StringUtils.isEmpty(userUpdateDto.getFatherName()) && !userUpdateDto.getFatherName().equals(user.getFatherName())) {
            user.setFatherName(userUpdateDto.getMotherName());
        }
        if(!StringUtils.isEmpty(userUpdateDto.getMotherName()) && !userUpdateDto.getMotherName().equals(user.getMotherName())) {
            user.setMotherName(userUpdateDto.getMotherName());
        }
        if(!StringUtils.isEmpty(userUpdateDto.getGuardianEmail()) && !userUpdateDto.getGuardianEmail().equals(user.getGuardianEmail())) {
            user.setGuardianEmail(userUpdateDto.getGuardianEmail());
        }
        if(!StringUtils.isEmpty(userUpdateDto.getGuardianContact()) && !userUpdateDto.getGuardianContact().equals(user.getGuardianContact())) {
            user.setGuardianContact(userUpdateDto.getGuardianContact());
        }
        if(userUpdateDto.getGrade() != null && !userUpdateDto.getGrade().equals(user.getGrade())) {
            user.setGrade(userUpdateDto.getGrade());
        }
        if(userUpdateDto.getCurriculum() != null && !userUpdateDto.getCurriculum().equals(user.getCurriculum())) {
            user.setCurriculum(userUpdateDto.getCurriculum());
        }
        if(userUpdateDto.getDateOfBirth() != null && !userUpdateDto.getDateOfBirth().equals(user.getDateOfBirth())) {
            user.setDateOfBirth(userUpdateDto.getDateOfBirth());
        }
        if(userUpdateDto.getLastSeenNotice() != null && !userUpdateDto.getLastSeenNotice().equals(user.getLastSeenNotice())) {
            user.setLastSeenNotice(userUpdateDto.getLastSeenNotice());
        }

        return userRepository.save(user);
    }

    public DataSummary getDataSummary() {
        long numberOfStudents = userRepository.countByRoles_Name(Role.ROLE_STUDENT);
        long numberOfTeachers = userRepository.countByRoles_Name(Role.ROLE_TEACHER);
        long numberOfUploadedContent = courseFileRepository.count();
        return new DataSummary(numberOfStudents, numberOfTeachers, numberOfUploadedContent);
    }

    public List<User> searchUsers(Long userId, String name, String roleName) {
        if(userId == null && name == null && roleName == null) {
            throw new BadRequestException(ResponseMessage.SEARCH_PARAMS_REQUIRED);
        }

        if(userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isPresent()) {
                return List.of(optionalUser.get());
            }
        }

        if(roleName != null) {
            List<User> users;
            try {
                users = userRepository.findByRoles_Name(Role.valueOf(roleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + roleName);
            }
            return name != null ?
                    users.stream()
                            .filter(user -> user.getFullName() != null &&
                                    user.getFullName().toLowerCase().contains(name.toLowerCase()))
                            .collect(Collectors.toList())
                    : users;
        } else {
            return name != null ?
                    userRepository.findByFullNameContainingIgnoreCase(name)
                    : Collections.emptyList();
        }
    }
}
