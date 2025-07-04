package com.tech11.usermanagement.actors.messages;

import java.time.LocalDateTime;

/**
 * User Messages
 * 
 * Contains all message types for user-related events.
 * These messages are used by actors to handle asynchronous operations.
 */
public class UserMessages {
    
    /**
     * User Created Message
     */
    public static class UserCreated {
        private final String userId;
        private final String email;
        private final String firstName;
        private final String lastName;
        private final String ipAddress;
        private final String userAgent;
        private final LocalDateTime timestamp;
        
        public UserCreated(String userId, String email, String firstName, String lastName, 
                          String ipAddress, String userAgent) {
            this.userId = userId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * User Updated Message
     */
    public static class UserUpdated {
        private final String userId;
        private final String email;
        private final String fieldChanged;
        private final String oldValue;
        private final String newValue;
        private final String changedBy;
        private final String ipAddress;
        private final LocalDateTime timestamp;
        
        public UserUpdated(String userId, String email, String fieldChanged, String oldValue, 
                          String newValue, String changedBy, String ipAddress) {
            this.userId = userId;
            this.email = email;
            this.fieldChanged = fieldChanged;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.changedBy = changedBy;
            this.ipAddress = ipAddress;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getFieldChanged() { return fieldChanged; }
        public String getOldValue() { return oldValue; }
        public String getNewValue() { return newValue; }
        public String getChangedBy() { return changedBy; }
        public String getIpAddress() { return ipAddress; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * User Deleted Message
     */
    public static class UserDeleted {
        private final String userId;
        private final String email;
        private final String deletedBy;
        private final String deletedByEmail;
        private final String reason;
        private final LocalDateTime timestamp;
        
        public UserDeleted(String userId, String email, String deletedBy, String deletedByEmail, String reason) {
            this.userId = userId;
            this.email = email;
            this.deletedBy = deletedBy;
            this.deletedByEmail = deletedByEmail;
            this.reason = reason;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getDeletedBy() { return deletedBy; }
        public String getDeletedByEmail() { return deletedByEmail; }
        public String getReason() { return reason; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Password Reset Message
     */
    public static class PasswordReset {
        private final String userId;
        private final String email;
        private final String resetToken;
        private final String ipAddress;
        private final LocalDateTime timestamp;
        
        public PasswordReset(String userId, String email, String resetToken, String ipAddress) {
            this.userId = userId;
            this.email = email;
            this.resetToken = resetToken;
            this.ipAddress = ipAddress;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getResetToken() { return resetToken; }
        public String getIpAddress() { return ipAddress; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * User Login Message
     */
    public static class UserLogin {
        private final String userId;
        private final String email;
        private final String ipAddress;
        private final String userAgent;
        private final boolean suspiciousLogin;
        private final LocalDateTime timestamp;
        
        public UserLogin(String userId, String email, String ipAddress, String userAgent, boolean suspiciousLogin) {
            this.userId = userId;
            this.email = email;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.suspiciousLogin = suspiciousLogin;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public boolean isSuspiciousLogin() { return suspiciousLogin; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * User Logout Message
     */
    public static class UserLogout {
        private final String userId;
        private final String email;
        private final String ipAddress;
        private final String userAgent;
        private final LocalDateTime timestamp;
        
        public UserLogout(String userId, String email, String ipAddress, String userAgent) {
            this.userId = userId;
            this.email = email;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
} 