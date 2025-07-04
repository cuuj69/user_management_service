package com.tech11.usermanagement.services;

import com.tech11.usermanagement.repository.UserRepository;
import com.tech11.usermanagement.validators.CreateUserRequestValidator;
import com.tech11.usermanagement.validators.UpdateUserRequestValidator;
import com.tech11.usermanagement.validators.ResetPasswordRequestValidator;
import com.tech11.usermanagement.data.PaginatedResponse;
import com.tech11.usermanagement.dto.request.CreateUserRequest;
import com.tech11.usermanagement.dto.request.ResetPasswordRequest;
import com.tech11.usermanagement.dto.request.UpdateUserRequest;
import com.tech11.usermanagement.dto.response.UserResponse;
import com.tech11.usermanagement.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.InternalServerErrorException;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 * Provides business logic for user CRUD operations.
 */
@ApplicationScoped
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Inject
    private UserRepository userRepository;

    @Inject
    private CreateUserRequestValidator createUserValidator;

    @Inject
    private UpdateUserRequestValidator updateUserValidator;

    @Inject
    private ResetPasswordRequestValidator resetPasswordValidator;

    public PaginatedResponse<UserResponse> getAllUsers(String firstName, String lastName, String email, int page, int size) {
        LOGGER.info("Getting all users with filters - firstName: %s, lastName: %s, email: %s, page: %d, size: %d"
                .formatted(firstName, lastName, email, page, size));
        
        try {
            // Validate pagination parameters
            if (page < 0) {
                LOGGER.warning("Invalid page number: " + page);
                throw new BadRequestException("Page number must be non-negative");
            }
            if (size <= 0 || size > 100) {
                LOGGER.warning("Invalid page size: " + size);
                throw new BadRequestException("Page size must be between 1 and 100");
            }
            
            List<User> users;
            long totalElements;
            
            if (firstName != null && !firstName.trim().isEmpty()) {
                LOGGER.info("Filtering users by firstName starting with: " + firstName);
                users = userRepository.findByFirstNameStartingWith(firstName.trim());
                totalElements = users.size();
            } else if (lastName != null && !lastName.trim().isEmpty()) {
                LOGGER.info("Filtering users by lastName starting with: " + lastName);
                users = userRepository.findByLastNameStartingWith(lastName.trim());
                totalElements = users.size();
            } else if (email != null && !email.trim().isEmpty()) {
                LOGGER.info("Filtering users by email starting with: " + email);
                users = userRepository.findByEmailStartingWith(email.trim());
                totalElements = users.size();
            } else {
                LOGGER.info("Getting all users with pagination");
                users = userRepository.findAll(page, size);
                totalElements = userRepository.count();
            }

            List<UserResponse> userResponses = users.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            LOGGER.info("Successfully retrieved %d users".formatted(userResponses.size()));
            return PaginatedResponse.of(userResponses, page, size, totalElements);
            
        } catch (BadRequestException e) {
            LOGGER.warning("Bad request in getAllUsers: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving users", e);
            throw new InternalServerErrorException("Failed to retrieve users: " + e.getMessage());
        }
    }

    public UserResponse getUserById(String id) {
        LOGGER.info("Getting user by ID: " + id);
        
        try {
            UUID uuid = convertHexToUUID(id);
            User user = userRepository.findById(uuid)
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
            
            LOGGER.info("Successfully retrieved user: " + user.getEmail());
            return convertToResponse(user);
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid user ID format: " + id + " - " + e.getMessage());
            throw new BadRequestException("Invalid user ID format: " + e.getMessage());
        } catch (NotFoundException e) {
            LOGGER.warning("User not found with ID: " + id);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user with ID: " + id, e);
            throw new InternalServerErrorException("Failed to retrieve user: " + e.getMessage());
        }
    }

    public UserResponse createUser(CreateUserRequest request) {
        LOGGER.info("Creating new user with email: " + request.getEmail());
        
        try {
            // Validate the request
            createUserValidator.validate(request);

            // Create new user
            User user = new User(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(), // In a real application, this should be hashed
                    request.getBirthday()
            );

            User savedUser = userRepository.save(user);
            LOGGER.info("Successfully created user with ID: " + savedUser.getId());
            return convertToResponse(savedUser);
            
        } catch (BadRequestException e) {
            LOGGER.warning("Bad request in createUser: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user with email: " + request.getEmail(), e);
            throw new InternalServerErrorException("Failed to create user: " + e.getMessage());
        }
    }

    public UserResponse updateUser(String id, UpdateUserRequest request) {
        LOGGER.info("Updating user with ID: " + id);
        
        try {
            // Validate the request
            UUID uuid = convertHexToUUID(id);
            updateUserValidator.validate(uuid, request);

            User user = userRepository.findById(uuid)
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

            // Update fields if provided
            if (request.getFirstName() != null) {
                LOGGER.info("Updating firstName for user: " + id);
                user.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                LOGGER.info("Updating lastName for user: " + id);
                user.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                LOGGER.info("Updating email for user: " + id + " to: " + request.getEmail());
                user.setEmail(request.getEmail());
            }
            if (request.getBirthday() != null) {
                LOGGER.info("Updating birthday for user: " + id);
                user.setBirthday(request.getBirthday());
            }

            User updatedUser = userRepository.update(user);
            LOGGER.info("Successfully updated user with ID: " + id);
            return convertToResponse(updatedUser);
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid user ID format: " + id + " - " + e.getMessage());
            throw new BadRequestException("Invalid user ID format: " + e.getMessage());
        } catch (BadRequestException e) {
            LOGGER.warning("Bad request in updateUser: " + e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            LOGGER.warning("User not found with ID: " + id);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user with ID: " + id, e);
            throw new InternalServerErrorException("Failed to update user: " + e.getMessage());
        }
    }

    public UserResponse resetPassword(String id, ResetPasswordRequest request) {
        LOGGER.info("Resetting password for user with ID: " + id);
        
        try {
            // Validate the request
            resetPasswordValidator.validate(request);

            UUID uuid = convertHexToUUID(id);
            User user = userRepository.findById(uuid)
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

            // Update password (in a real application, this should be hashed)
            user.setPassword(request.getNewPassword());
            LOGGER.info("Password updated for user: " + id);

            User updatedUser = userRepository.update(user);
            LOGGER.info("Successfully reset password for user with ID: " + id);
            return convertToResponse(updatedUser);
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid user ID format: " + id + " - " + e.getMessage());
            throw new BadRequestException("Invalid user ID format: " + e.getMessage());
        } catch (BadRequestException e) {
            LOGGER.warning("Bad request in resetPassword: " + e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            LOGGER.warning("User not found with ID: " + id);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error resetting password for user with ID: " + id, e);
            throw new InternalServerErrorException("Failed to reset password: " + e.getMessage());
        }
    }

    public void deleteUser(String id) {
        LOGGER.info("Deleting user with ID: " + id);
        
        try {
            UUID uuid = convertHexToUUID(id);
            boolean deleted = userRepository.deleteById(uuid);
            if (!deleted) {
                LOGGER.warning("User not found for deletion with ID: " + id);
                throw new NotFoundException("User not found with id: " + id);
            }
            
            LOGGER.info("Successfully deleted user with ID: " + id);
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid user ID format: " + id + " - " + e.getMessage());
            throw new BadRequestException("Invalid user ID format: " + e.getMessage());
        } catch (NotFoundException e) {
            LOGGER.warning("User not found with ID: " + id);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user with ID: " + id, e);
            throw new InternalServerErrorException("Failed to delete user: " + e.getMessage());
        }
    }

    /**
     * Convert User entity to UserResponse.
     *
     * @param user the user entity
     * @return user response
     */
    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthday(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getVersion()
        );
    }

    /**
     * Convert hex string (without hyphens) to UUID.
     *
     * @param hexId the hex string ID
     * @return UUID
     */
    private UUID convertHexToUUID(String hexId) {
        try {
            if (hexId == null || hexId.trim().isEmpty()) {
                throw new IllegalArgumentException("Hex ID cannot be null or empty");
            }
            
            if (hexId.length() != 32) {
                throw new IllegalArgumentException("Invalid hex ID length: " + hexId.length() + ". Expected 32 characters.");
            }
            
            String uuidString = hexId.substring(0, 8) + "-" +
                               hexId.substring(8, 12) + "-" +
                               hexId.substring(12, 16) + "-" +
                               hexId.substring(16, 20) + "-" +
                               hexId.substring(20, 32);
            
            return UUID.fromString(uuidString);
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Failed to convert hex ID to UUID: " + hexId + " - " + e.getMessage());
            throw e;
        }
    }
} 