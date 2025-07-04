package com.tech11.usermanagement.actors;

import com.tech11.usermanagement.actors.messages.UserMessages;
import com.tech11.usermanagement.services.AuditService;
import com.tech11.usermanagement.services.NotificationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * User Actor
 * 
 * Handles user-related asynchronous operations:
 * - User creation events
 * - User update events
 * - User deletion events
 * - User activity tracking
 */
@ApplicationScoped
public class UserActor {
    
    private static final Logger LOGGER = Logger.getLogger(UserActor.class.getName());
    
    @Inject
    private NotificationService notificationService;
    
    @Inject
    private AuditService auditService;
    
    /**
     * Handle user creation event
     * 
     * @param message user creation message
     */
    public void handleUserCreated(UserMessages.UserCreated message) {
        LOGGER.info("UserActor: Handling user creation for: " + message.getEmail());
        
        try {
            // Send welcome email
            notificationService.sendWelcomeEmail(message.getEmail(), message.getFirstName());
            
            // Log audit event
            auditService.logAuthenticationEvent(
                message.getUserId(), 
                message.getEmail(), 
                "USER_CREATED", 
                message.getIpAddress(), 
                message.getUserAgent()
            );
            
            LOGGER.info("UserActor: Successfully processed user creation for: " + message.getEmail());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserActor: Failed to process user creation for: " + message.getEmail(), e);
        }
    }
    
    /**
     * Handle user update event
     * 
     * @param message user update message
     */
    public void handleUserUpdated(UserMessages.UserUpdated message) {
        LOGGER.info("UserActor: Handling user update for: " + message.getEmail());
        
        try {
            // Log profile change audit
            auditService.logProfileChange(
                message.getUserId(),
                message.getEmail(),
                message.getFieldChanged(),
                message.getOldValue(),
                message.getNewValue(),
                message.getChangedBy()
            );
            
            // Send notification if email was changed
            if ("email".equals(message.getFieldChanged())) {
                notificationService.sendVerificationEmail(message.getNewValue(), "verification-token");
            }
            
            LOGGER.info("UserActor: Successfully processed user update for: " + message.getEmail());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserActor: Failed to process user update for: " + message.getEmail(), e);
        }
    }
    
    /**
     * Handle user deletion event
     * 
     * @param message user deletion message
     */
    public void handleUserDeleted(UserMessages.UserDeleted message) {
        LOGGER.info("UserActor: Handling user deletion for: " + message.getEmail());
        
        try {
            // Log deletion audit
            auditService.logAdminAction(
                message.getDeletedBy(),
                message.getDeletedByEmail(),
                "USER_DELETED",
                message.getUserId(),
                "User account deleted"
            );
            
            // Send account deletion notification
            notificationService.sendSecurityAlert(
                message.getEmail(),
                "ACCOUNT_DELETED",
                "Your account has been deleted"
            );
            
            LOGGER.info("UserActor: Successfully processed user deletion for: " + message.getEmail());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserActor: Failed to process user deletion for: " + message.getEmail(), e);
        }
    }
    
    /**
     * Handle password reset event
     * 
     * @param message password reset message
     */
    public void handlePasswordReset(UserMessages.PasswordReset message) {
        LOGGER.info("UserActor: Handling password reset for: " + message.getEmail());
        
        try {
            // Send password reset email
            notificationService.sendPasswordResetEmail(message.getEmail(), message.getResetToken());
            
            // Log security event
            auditService.logSecurityEvent(
                "PASSWORD_RESET_REQUESTED",
                "MEDIUM",
                message.getUserId(),
                "Password reset requested",
                message.getIpAddress()
            );
            
            LOGGER.info("UserActor: Successfully processed password reset for: " + message.getEmail());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserActor: Failed to process password reset for: " + message.getEmail(), e);
        }
    }
    
    /**
     * Handle user login event
     * 
     * @param message user login message
     */
    public void handleUserLogin(UserMessages.UserLogin message) {
        LOGGER.info("UserActor: Handling user login for: " + message.getEmail());
        
        try {
            // Log authentication event
            auditService.logAuthenticationEvent(
                message.getUserId(),
                message.getEmail(),
                "LOGIN",
                message.getIpAddress(),
                message.getUserAgent()
            );
            
            // Check for suspicious login patterns
            if (message.isSuspiciousLogin()) {
                notificationService.sendSecurityAlert(
                    message.getEmail(),
                    "SUSPICIOUS_LOGIN",
                    "Suspicious login detected from: " + message.getIpAddress()
                );
            }
            
            LOGGER.info("UserActor: Successfully processed user login for: " + message.getEmail());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserActor: Failed to process user login for: " + message.getEmail(), e);
        }
    }
    
    /**
     * Handle user logout event
     * 
     * @param message user logout message
     */
    public void handleUserLogout(UserMessages.UserLogout message) {
        LOGGER.info("UserActor: Handling user logout for: " + message.getEmail());
        
        try {
            // Log logout event
            auditService.logAuthenticationEvent(
                message.getUserId(),
                message.getEmail(),
                "LOGOUT",
                message.getIpAddress(),
                message.getUserAgent()
            );
            
            LOGGER.info("UserActor: Successfully processed user logout for: " + message.getEmail());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserActor: Failed to process user logout for: " + message.getEmail(), e);
        }
    }
} 