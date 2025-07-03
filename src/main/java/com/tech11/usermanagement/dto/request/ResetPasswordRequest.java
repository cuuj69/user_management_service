package com.tech11.usermanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for resetting user password")
public class ResetPasswordRequest {

    @Schema(description = "New password", example = "newSecurePassword123", required = true)
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long")
    private String newPassword;

    // Default constructor
    public ResetPasswordRequest() {}

    // Constructor with password
    public ResetPasswordRequest(String newPassword) {
        this.newPassword = newPassword;
    }

    // Getters and Setters
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "newPassword='[HIDDEN]'" +
                '}';
    }
} 