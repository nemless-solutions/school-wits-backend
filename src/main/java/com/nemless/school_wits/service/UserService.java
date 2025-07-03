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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final CourseFileRepository courseFileRepository;

    public List<User> getAllUserByRole(String roleName) {
        try {
            return userRepository.findByRoles_Name(Role.valueOf(roleName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + roleName);
        }
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_USER_ID));
    }

    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessage.INVALID_USER_ID));

        if(!user.getEmail().equals(userUpdateDto.getEmail()) && userRepository.existsByEmail(userUpdateDto.getEmail())) {
            throw new BadRequestException(ResponseMessage.EMAIL_EXISTS);
        } else {
            user.setEmail(userUpdateDto.getEmail());
        }
        user.setFullName(userUpdateDto.getFullName());
        user.setCurrentSchool(userUpdateDto.getCurrentSchool());
        user.setFatherName(userUpdateDto.getMotherName());
        user.setMotherName(userUpdateDto.getMotherName());
        user.setGuardianEmail(userUpdateDto.getGuardianEmail());
        user.setGuardianContact(userUpdateDto.getGuardianContact());
        user.setGrade(userUpdateDto.getGrade());
        user.setDateOfBirth(userUpdateDto.getDateOfBirth());
        user.setLastSeenNotice(userUpdateDto.getLastSeenNotice());

        return userRepository.save(user);
    }

    public DataSummary getDataSummary() {
        long numberOfStudents = userRepository.countByRoles_Name(Role.ROLE_STUDENT);
        long numberOfTeachers = userRepository.countByRoles_Name(Role.ROLE_TEACHER);
        long numberOfUploadedContent = courseFileRepository.count();
        return new DataSummary(numberOfStudents, numberOfTeachers, numberOfUploadedContent);
    }
}
