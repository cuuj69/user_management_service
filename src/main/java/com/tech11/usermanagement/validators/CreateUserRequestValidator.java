package com.tech11.usermanagement.validators;

import com.tech11.usermanagement.dto.request.CreateUserRequest;
import com.tech11.usermanagement.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class CreateUserRequestValidator {
    @Inject
    UserRepository userRepository;

    public void validate(CreateUserRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new BadRequestException("First name is required");
        }
        if (request.getLastName() == null || request.getLastName().isBlank()) {
            throw new BadRequestException("Last name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }
        if (request.getBirthday() == null) {
            throw new BadRequestException("Birthday is required");
        }
    }
} 