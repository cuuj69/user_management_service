package com.tech11.usermanagement.validators;

import com.tech11.usermanagement.dto.request.ResetPasswordRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class ResetPasswordRequestValidator {
    public void validate(ResetPasswordRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }
    }
} 