package com.tech11.usermanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Error response object")
@JsonbPropertyOrder({"statusCode", "message", "timestamp", "errors"})
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int statusCode;

    @Schema(description = "Error message", example = "Validation failed")
    private String message;

    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "List of validation errors")
    private List<ValidationError> errors;

    // Default constructor
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with all fields
    public ErrorResponse(int statusCode, String message, List<ValidationError> errors) {
        this();
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

    // Constructor without validation errors
    public ErrorResponse(int statusCode, String message) {
        this(statusCode, message, null);
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    // Inner class for validation errors
    @Schema(description = "Validation error details")
    @JsonbPropertyOrder({"field", "message", "rejectedValue"})
    public static class ValidationError {

        @Schema(description = "Field name that failed validation", example = "email")
        private String field;

        @Schema(description = "Validation error message", example = "Email must be a valid email address")
        private String message;

        @Schema(description = "Value that was rejected", example = "invalid-email")
        private String rejectedValue;

        public ValidationError() {}

        public ValidationError(String field, String message, String rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        public ValidationError(String field, String message) {
            this(field, message, null);
        }

        // Getters and Setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(String rejectedValue) {
            this.rejectedValue = rejectedValue;
        }
    }
} 