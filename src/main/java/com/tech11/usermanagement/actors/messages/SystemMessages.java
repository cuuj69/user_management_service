package com.tech11.usermanagement.actors.messages;

import java.time.LocalDateTime;

/**
 * System Messages
 * 
 * Contains all message types for system-level events.
 * These messages are used by actors to handle asynchronous system operations.
 */
public class SystemMessages {
    
    /**
     * System Startup Message
     */
    public static class SystemStartup {
        private final String version;
        private final String environment;
        private final LocalDateTime timestamp;
        
        public SystemStartup(String version, String environment) {
            this.version = version;
            this.environment = environment;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getVersion() { return version; }
        public String getEnvironment() { return environment; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * System Shutdown Message
     */
    public static class SystemShutdown {
        private final String reason;
        private final LocalDateTime timestamp;
        
        public SystemShutdown(String reason) {
            this.reason = reason;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getReason() { return reason; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Performance Alert Message
     */
    public static class PerformanceAlert {
        private final String alertType;
        private final String component;
        private final String details;
        private final String severity;
        private final double metricValue;
        private final LocalDateTime timestamp;
        
        public PerformanceAlert(String alertType, String component, String details, 
                               String severity, double metricValue) {
            this.alertType = alertType;
            this.component = component;
            this.details = details;
            this.severity = severity;
            this.metricValue = metricValue;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getAlertType() { return alertType; }
        public String getComponent() { return component; }
        public String getDetails() { return details; }
        public String getSeverity() { return severity; }
        public double getMetricValue() { return metricValue; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Database Maintenance Message
     */
    public static class DatabaseMaintenance {
        private final String maintenanceType;
        private final String details;
        private final long durationMs;
        private final boolean success;
        private final LocalDateTime timestamp;
        
        public DatabaseMaintenance(String maintenanceType, String details, long durationMs, boolean success) {
            this.maintenanceType = maintenanceType;
            this.details = details;
            this.durationMs = durationMs;
            this.success = success;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getMaintenanceType() { return maintenanceType; }
        public String getDetails() { return details; }
        public long getDurationMs() { return durationMs; }
        public boolean isSuccess() { return success; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Security Alert Message
     */
    public static class SecurityAlert {
        private final String alertType;
        private final String severity;
        private final String userId;
        private final String details;
        private final String ipAddress;
        private final LocalDateTime timestamp;
        
        public SecurityAlert(String alertType, String severity, String userId, String details, String ipAddress) {
            this.alertType = alertType;
            this.severity = severity;
            this.userId = userId;
            this.details = details;
            this.ipAddress = ipAddress;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getAlertType() { return alertType; }
        public String getSeverity() { return severity; }
        public String getUserId() { return userId; }
        public String getDetails() { return details; }
        public String getIpAddress() { return ipAddress; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Backup Message
     */
    public static class Backup {
        private final String backupType;
        private final String details;
        private final long sizeBytes;
        private final boolean success;
        private final LocalDateTime timestamp;
        
        public Backup(String backupType, String details, long sizeBytes, boolean success) {
            this.backupType = backupType;
            this.details = details;
            this.sizeBytes = sizeBytes;
            this.success = success;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getBackupType() { return backupType; }
        public String getDetails() { return details; }
        public long getSizeBytes() { return sizeBytes; }
        public boolean isSuccess() { return success; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
} 