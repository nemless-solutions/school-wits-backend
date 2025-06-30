package com.nemless.school_wits.controller;

import com.nemless.school_wits.dto.request.ContactRequestDto;
import com.nemless.school_wits.model.ContactRequest;
import com.nemless.school_wits.service.ContactRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/contact_request")
public class ContactRequestController {
    private final ContactRequestService contactRequestService;

    @PostMapping
    ResponseEntity<String> saveContactRequest(@Valid @RequestBody ContactRequestDto contactRequestDto) {
        log.info("New contact request: {}", contactRequestDto);
        contactRequestService.saveContactRequest(contactRequestDto);
        return ResponseEntity.ok("Message submitted, we'll contact soon.");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<List<ContactRequest>> getAllContactRequests() {
        return ResponseEntity.ok(contactRequestService.getAllContactRequests());
    }
}
