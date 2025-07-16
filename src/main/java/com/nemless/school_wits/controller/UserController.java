package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.UserUpdateDto;
import com.nemless.school_wits.dto.response.DataSummary;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<List<User>> getAllUserByRole(@RequestParam String roleName) {
        return ResponseEntity.ok(userService.getAllUserByRole(roleName));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<List<User>> searchUser(@RequestParam(required = false) Long userId, @RequestParam(required = false) String name) {
        return ResponseEntity.ok(userService.searchUsers(userId, name));
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        log.info("Updating user {}: {}", userId, userUpdateDto);
        return ResponseEntity.ok(userService.updateUser(userId, userUpdateDto));
    }

    @GetMapping("/admin/summary")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<DataSummary> getDataSummary() {
        return ResponseEntity.ok(userService.getDataSummary());
    }
}
