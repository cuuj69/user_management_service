package com.tech11.usermanagement.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(description = "Standard API response wrapper")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"statusCode", "message", "data"})
@JsonbPropertyOrder({"statusCode", "message", "data"})
public class ApiResponse<T> {

    @Schema(description = "HTTP status code", example = "200")
    private int statusCode;

    @Schema(description = "Response message", example = "Success")
    private String message;

    @Schema(description = "Response data wrapper")
    private DataWrapper<T> data;

    // Default constructor
    public ApiResponse() {
    }

    // Constructor with all fields
    public ApiResponse(int statusCode, String message, T data, Long processingTimeMs) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = new DataWrapper<>(data, processingTimeMs);
    }

    // Static factory methods for common responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data, null);
    }

    public static <T> ApiResponse<T> success(T data, Long processingTimeMs) {
        return new ApiResponse<>(200, "Success (in " + processingTimeMs + "ms)", data, processingTimeMs);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Created successfully", data, null);
    }

    public static <T> ApiResponse<T> created(T data, Long processingTimeMs) {
        return new ApiResponse<>(201, "Created successfully (in " + processingTimeMs + "ms)", data, processingTimeMs);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(204, "No content", null, null);
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return new ApiResponse<>(statusCode, message, null, null);
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

    public DataWrapper<T> getData() {
        return data;
    }

    public void setData(DataWrapper<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    // Inner class for data wrapper
    @Schema(description = "Data wrapper containing pageData and actual data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"pageData", "data"})
    @JsonbPropertyOrder({"pageData", "data"})
    public static class DataWrapper<T> {
        
        @Schema(description = "Pagination information")
        private PageData pageData;
        
        @Schema(description = "Actual response data")
        private T data;

        public DataWrapper(T data, Long processingTimeMs) {
            this.data = data;
            // For single items, create a simple pageData
            this.pageData = new PageData(1, 1, 1, 1, 1);
        }

        public DataWrapper(T data, PageData pageData) {
            this.data = data;
            this.pageData = pageData;
        }

        // Getters and Setters
        public PageData getPageData() {
            return pageData;
        }

        public void setPageData(PageData pageData) {
            this.pageData = pageData;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    // Inner class for page data
    @Schema(description = "Pagination information")
    @JsonPropertyOrder({"currentPage", "pageSize", "totalPages", "totalItemsInPage", "totalItems"})
    @JsonbPropertyOrder({"currentPage", "pageSize", "totalPages", "totalItemsInPage", "totalItems"})
    public static class PageData {
        
        @Schema(description = "Current page number", example = "1")
        private int currentPage;
        
        @Schema(description = "Page size", example = "50")
        private int pageSize;
        
        @Schema(description = "Total number of pages", example = "6")
        private int totalPages;
        
        @Schema(description = "Total items in current page", example = "50")
        private int totalItemsInPage;
        
        @Schema(description = "Total number of items", example = "291")
        private int totalItems;

        public PageData(int currentPage, int pageSize, int totalPages, int totalItemsInPage, int totalItems) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalItemsInPage = totalItemsInPage;
            this.totalItems = totalItems;
        }

        // Getters and Setters
        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalItemsInPage() {
            return totalItemsInPage;
        }

        public void setTotalItemsInPage(int totalItemsInPage) {
            this.totalItemsInPage = totalItemsInPage;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
        }
    }
} 