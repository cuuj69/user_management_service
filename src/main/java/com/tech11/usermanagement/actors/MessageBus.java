package com.tech11.usermanagement.actors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.logging.Logger;

import com.tech11.usermanagement.actors.messages.SystemMessages;
import com.tech11.usermanagement.actors.messages.UserMessages;

import java.util.logging.Level;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Message Bus
 * 
 * Coordinates message passing between actors.
 * Handles asynchronous message processing and routing.
 */
@ApplicationScoped
public class MessageBus {
    
    private static final Logger LOGGER = Logger.getLogger(MessageBus.class.getName());
    
    @Inject
    private UserActor userActor;
    
    @Inject
    private SystemActor systemActor;
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    /**
     * Send user created message
     * 
     * @param message user created message
     */
    public void sendUserCreated(UserMessages.UserCreated message) {
        LOGGER.info("MessageBus: Sending user created message for: " + message.getEmail());
        
        CompletableFuture.runAsync(() -> {
            try {
                userActor.handleUserCreated(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle user created message", e);
            }
        }, executorService);
    }
    
    /**
     * Send user updated message
     * 
     * @param message user updated message
     */
    public void sendUserUpdated(UserMessages.UserUpdated message) {
        LOGGER.info("MessageBus: Sending user updated message for: " + message.getEmail());
        
        CompletableFuture.runAsync(() -> {
            try {
                userActor.handleUserUpdated(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle user updated message", e);
            }
        }, executorService);
    }
    
    /**
     * Send user deleted message
     * 
     * @param message user deleted message
     */
    public void sendUserDeleted(UserMessages.UserDeleted message) {
        LOGGER.info("MessageBus: Sending user deleted message for: " + message.getEmail());
        
        CompletableFuture.runAsync(() -> {
            try {
                userActor.handleUserDeleted(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle user deleted message", e);
            }
        }, executorService);
    }
    
    /**
     * Send password reset message
     * 
     * @param message password reset message
     */
    public void sendPasswordReset(UserMessages.PasswordReset message) {
        LOGGER.info("MessageBus: Sending password reset message for: " + message.getEmail());
        
        CompletableFuture.runAsync(() -> {
            try {
                userActor.handlePasswordReset(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle password reset message", e);
            }
        }, executorService);
    }
    
    /**
     * Send user login message
     * 
     * @param message user login message
     */
    public void sendUserLogin(UserMessages.UserLogin message) {
        LOGGER.info("MessageBus: Sending user login message for: " + message.getEmail());
        
        CompletableFuture.runAsync(() -> {
            try {
                userActor.handleUserLogin(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle user login message", e);
            }
        }, executorService);
    }
    
    /**
     * Send user logout message
     * 
     * @param message user logout message
     */
    public void sendUserLogout(UserMessages.UserLogout message) {
        LOGGER.info("MessageBus: Sending user logout message for: " + message.getEmail());
        
        CompletableFuture.runAsync(() -> {
            try {
                userActor.handleUserLogout(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle user logout message", e);
            }
        }, executorService);
    }
    
    /**
     * Send system startup message
     * 
     * @param message system startup message
     */
    public void sendSystemStartup(SystemMessages.SystemStartup message) {
        LOGGER.info("MessageBus: Sending system startup message");
        
        CompletableFuture.runAsync(() -> {
            try {
                systemActor.handleSystemStartup(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle system startup message", e);
            }
        }, executorService);
    }
    
    /**
     * Send system shutdown message
     * 
     * @param message system shutdown message
     */
    public void sendSystemShutdown(SystemMessages.SystemShutdown message) {
        LOGGER.info("MessageBus: Sending system shutdown message");
        
        CompletableFuture.runAsync(() -> {
            try {
                systemActor.handleSystemShutdown(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle system shutdown message", e);
            }
        }, executorService);
    }
    
    /**
     * Send performance alert message
     * 
     * @param message performance alert message
     */
    public void sendPerformanceAlert(SystemMessages.PerformanceAlert message) {
        LOGGER.info("MessageBus: Sending performance alert message: " + message.getAlertType());
        
        CompletableFuture.runAsync(() -> {
            try {
                systemActor.handlePerformanceAlert(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle performance alert message", e);
            }
        }, executorService);
    }
    
    /**
     * Send database maintenance message
     * 
     * @param message database maintenance message
     */
    public void sendDatabaseMaintenance(SystemMessages.DatabaseMaintenance message) {
        LOGGER.info("MessageBus: Sending database maintenance message: " + message.getMaintenanceType());
        
        CompletableFuture.runAsync(() -> {
            try {
                systemActor.handleDatabaseMaintenance(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle database maintenance message", e);
            }
        }, executorService);
    }
    
    /**
     * Send security alert message
     * 
     * @param message security alert message
     */
    public void sendSecurityAlert(SystemMessages.SecurityAlert message) {
        LOGGER.info("MessageBus: Sending security alert message: " + message.getAlertType());
        
        CompletableFuture.runAsync(() -> {
            try {
                systemActor.handleSecurityAlert(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle security alert message", e);
            }
        }, executorService);
    }
    
    /**
     * Send backup message
     * 
     * @param message backup message
     */
    public void sendBackup(SystemMessages.Backup message) {
        LOGGER.info("MessageBus: Sending backup message: " + message.getBackupType());
        
        CompletableFuture.runAsync(() -> {
            try {
                systemActor.handleBackup(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MessageBus: Failed to handle backup message", e);
            }
        }, executorService);
    }
    
    /**
     * Shutdown the message bus
     */
    public void shutdown() {
        LOGGER.info("MessageBus: Shutting down message bus");
        executorService.shutdown();
    }
} 