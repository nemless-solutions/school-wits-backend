package com.nemless.school_wits.service;

import com.nemless.school_wits.dto.request.ContactRequestDto;
import com.nemless.school_wits.model.ContactRequest;
import com.nemless.school_wits.repository.ContactRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactRequestService {
    private final ContactRequestRepository contactRequestRepository;

    public void saveContactRequest(ContactRequestDto contactRequestDto) {
        ContactRequest contactRequest = ContactRequest.builder()
                .fullName(contactRequestDto.getFullName())
                .email(contactRequestDto.getEmail())
                .contact(contactRequestDto.getContact())
                .message(contactRequestDto.getMessage())
                .build();
        contactRequestRepository.save(contactRequest);
    }

    public List<ContactRequest> getAllContactRequests() {
        return contactRequestRepository.findAll();
    }
}
