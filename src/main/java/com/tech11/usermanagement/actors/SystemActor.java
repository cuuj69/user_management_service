package com.tech11.usermanagement.actors;

import com.tech11.usermanagement.actors.messages.SystemMessages;
import com.tech11.usermanagement.services.AuditService;
import com.tech11.usermanagement.services.NotificationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * System Actor
 * 
 * Handles system-level asynchronous operations:
 * - System health monitoring
 * - Performance metrics collection
 * - Maintenance operations
 * - System alerts
 */
@ApplicationScoped
public class SystemActor {
    
    private static final Logger LOGGER = Logger.getLogger(SystemActor.class.getName());
    
    @Inject
    private AuditService auditService;
    
    @Inject
    private NotificationService notificationService;
    
    /**
     * Handle system startup event
     * 
     * @param message system startup message
     */
    public void handleSystemStartup(SystemMessages.SystemStartup message) {
        LOGGER.info("SystemActor: Handling system startup");
        
        try {
            // Log system startup
            auditService.logSystemEvent(
                "SYSTEM_STARTUP",
                "Application",
                "User Management Service started successfully",
                "INFO"
            );
            
            // Send startup notification to administrators
            notificationService.sendSecurityAlert(
                "admin@tech11.com",
                "SYSTEM_STARTUP",
                "User Management Service has started successfully"
            );
            
            LOGGER.info("SystemActor: Successfully processed system startup");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SystemActor: Failed to process system startup", e);
        }
    }
    
    /**
     * Handle system shutdown event
     * 
     * @param message system shutdown message
     */
    public void handleSystemShutdown(SystemMessages.SystemShutdown message) {
        LOGGER.info("SystemActor: Handling system shutdown");
        
        try {
            // Log system shutdown
            auditService.logSystemEvent(
                "SYSTEM_SHUTDOWN",
                "Application",
                "User Management Service shutting down",
                "INFO"
            );
            
            // Send shutdown notification to administrators
            notificationService.sendSecurityAlert(
                "admin@tech11.com",
                "SYSTEM_SHUTDOWN",
                "User Management Service is shutting down"
            );
            
            LOGGER.info("SystemActor: Successfully processed system shutdown");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SystemActor: Failed to process system shutdown", e);
        }
    }
    
    /**
     * Handle performance alert
     * 
     * @param message performance alert message
     */
    public void handlePerformanceAlert(SystemMessages.PerformanceAlert message) {
        LOGGER.info("SystemActor: Handling performance alert - " + message.getAlertType());
        
        try {
            // Log performance event
            auditService.logSystemEvent(
                "PERFORMANCE_ALERT",
                message.getComponent(),
                message.getDetails(),
                message.getSeverity()
            );
            
            // Send performance alert to administrators
            notificationService.sendSecurityAlert(
                "admin@tech11.com",
                "PERFORMANCE_ALERT",
                "Performance alert: " + message.getAlertType() + " - " + message.getDetails()
            );
            
            LOGGER.info("SystemActor: Successfully processed performance alert");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SystemActor: Failed to process performance alert", e);
        }
    }
    
    /**
     * Handle database maintenance event
     * 
     * @param message database maintenance message
     */
    public void handleDatabaseMaintenance(SystemMessages.DatabaseMaintenance message) {
        LOGGER.info("SystemActor: Handling database maintenance - " + message.getMaintenanceType());
        
        try {
            // Log maintenance event
            auditService.logSystemEvent(
                "DATABASE_MAINTENANCE",
                "Database",
                "Database maintenance: " + message.getMaintenanceType(),
                "INFO"
            );
            
            // Send maintenance notification
            notificationService.sendSecurityAlert(
                "admin@tech11.com",
                "DATABASE_MAINTENANCE",
                "Database maintenance completed: " + message.getMaintenanceType()
            );
            
            LOGGER.info("SystemActor: Successfully processed database maintenance");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SystemActor: Failed to process database maintenance", e);
        }
    }
    
    /**
     * Handle security alert
     * 
     * @param message security alert message
     */
    public void handleSecurityAlert(SystemMessages.SecurityAlert message) {
        LOGGER.info("SystemActor: Handling security alert - " + message.getAlertType());
        
        try {
            // Log security event
            auditService.logSecurityEvent(
                message.getAlertType(),
                message.getSeverity(),
                message.getUserId(),
                message.getDetails(),
                message.getIpAddress()
            );
            
            // Send security alert to administrators
            notificationService.sendSecurityAlert(
                "admin@tech11.com",
                "SECURITY_ALERT",
                "Security alert: " + message.getAlertType() + " - " + message.getDetails()
            );
            
            LOGGER.info("SystemActor: Successfully processed security alert");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SystemActor: Failed to process security alert", e);
        }
    }
    
    /**
     * Handle backup event
     * 
     * @param message backup message
     */
    public void handleBackup(SystemMessages.Backup message) {
        LOGGER.info("SystemActor: Handling backup - " + message.getBackupType());
        
        try {
            // Log backup event
            auditService.logSystemEvent(
                "BACKUP",
                "Database",
                "Backup completed: " + message.getBackupType() + " - " + message.getDetails(),
                "INFO"
            );
            
            // Send backup notification
            notificationService.sendSecurityAlert(
                "admin@tech11.com",
                "BACKUP_COMPLETED",
                "Backup completed: " + message.getBackupType()
            );
            
            LOGGER.info("SystemActor: Successfully processed backup");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SystemActor: Failed to process backup", e);
        }
    }
} 