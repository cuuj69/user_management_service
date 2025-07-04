package com.tech11.usermanagement.service;

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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 * Provides business logic for user CRUD operations.
 */
@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private CreateUserRequestValidator createUserValidator;

    @Inject
    private UpdateUserRequestValidator updateUserValidator;

    @Inject
    private ResetPasswordRequestValidator resetPasswordValidator;

    public PaginatedResponse<UserResponse> getAllUsers(String firstName, String lastName, String email, int page, int size) {
        // Validate pagination parameters
        if (page < 0) {
            throw new BadRequestException("Page number must be non-negative");
        }
        if (size <= 0 || size > 100) {
            throw new BadRequestException("Page size must be between 1 and 100");
        }
        
        List<User> users;
        long totalElements;
        
        if (firstName != null && !firstName.trim().isEmpty()) {
            users = userRepository.findByFirstNameStartingWith(firstName.trim());
            totalElements = users.size();
        } else if (lastName != null && !lastName.trim().isEmpty()) {
            users = userRepository.findByLastNameStartingWith(lastName.trim());
            totalElements = users.size();
        } else if (email != null && !email.trim().isEmpty()) {
            users = userRepository.findByEmailStartingWith(email.trim());
            totalElements = users.size();
        } else {
            users = userRepository.findAll(page, size);
            totalElements = userRepository.count();
        }

        List<UserResponse> userResponses = users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.of(userResponses, page, size, totalElements);
    }

    public UserResponse getUserById(String id) {
        UUID uuid = convertHexToUUID(id);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return convertToResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request) {
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
        return convertToResponse(savedUser);
    }

    public UserResponse updateUser(String id, UpdateUserRequest request) {
        // Validate the request
        UUID uuid = convertHexToUUID(id);
        updateUserValidator.validate(uuid, request);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Update fields if provided
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }

        User updatedUser = userRepository.update(user);
        return convertToResponse(updatedUser);
    }

    public UserResponse resetPassword(String id, ResetPasswordRequest request) {
        // Validate the request
        resetPasswordValidator.validate(request);

        UUID uuid = convertHexToUUID(id);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Update password (in a real application, this should be hashed)
        user.setPassword(request.getNewPassword());

        User updatedUser = userRepository.update(user);
        return convertToResponse(updatedUser);
    }

    public void deleteUser(String id) {
        UUID uuid = convertHexToUUID(id);
        boolean deleted = userRepository.deleteById(uuid);
        if (!deleted) {
            throw new NotFoundException("User not found with id: " + id);
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
        if (hexId.length() != 32) {
            throw new IllegalArgumentException("Invalid hex ID length: " + hexId.length());
        }
        
        String uuidString = hexId.substring(0, 8) + "-" +
                           hexId.substring(8, 12) + "-" +
                           hexId.substring(12, 16) + "-" +
                           hexId.substring(16, 20) + "-" +
                           hexId.substring(20, 32);
        
        return UUID.fromString(uuidString);
    }
} 