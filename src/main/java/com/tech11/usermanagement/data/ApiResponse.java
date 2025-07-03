package com.tech11.usermanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Generic API response wrapper")
public class ApiResponse<T> {

    @Schema(description = "Response status", example = "success")
    private String status;

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response data")
    private T data;

    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Error code (if applicable)", example = "USER_NOT_FOUND")
    private String errorCode;

    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Success constructor
    public ApiResponse(String message, T data) {
        this();
        this.status = "success";
        this.message = message;
        this.data = data;
    }

    // Error constructor
    public ApiResponse(String message, String errorCode) {
        this();
        this.status = "error";
        this.message = message;
        this.errorCode = errorCode;
    }

    // Full constructor
    public ApiResponse(String status, String message, T data, String errorCode) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    // Static factory methods
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("Operation completed successfully", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(message, errorCode);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
} 