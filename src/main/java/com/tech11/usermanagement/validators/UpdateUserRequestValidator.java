package com.tech11.usermanagement.validators;

import com.tech11.usermanagement.dto.request.UpdateUserRequest;
import com.tech11.usermanagement.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class UpdateUserRequestValidator {
    @Inject
    UserRepository userRepository;

    public void validate(Long id, UpdateUserRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }
        if (request.getEmail() != null && userRepository.existsByEmailExceptId(request.getEmail(), id)) {
            throw new BadRequestException("Email already exists");
        }
        if (request.getFirstName() != null && request.getFirstName().isBlank()) {
            throw new BadRequestException("First name cannot be blank");
        }
        if (request.getLastName() != null && request.getLastName().isBlank()) {
            throw new BadRequestException("Last name cannot be blank");
        }

    }
} 