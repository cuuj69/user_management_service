package com.tech11.usermanagement.services;

import com.tech11.usermanagement.repository.UserRepository;
import com.tech11.usermanagement.service.UserService;
import com.tech11.usermanagement.data.PaginatedResponse;
import com.tech11.usermanagement.dto.request.CreateUserRequest;
import com.tech11.usermanagement.dto.request.ResetPasswordRequest;
import com.tech11.usermanagement.dto.request.UpdateUserRequest;
import com.tech11.usermanagement.dto.response.UserResponse;
import com.tech11.usermanagement.entity.User;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserResponse testUserResponse;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;
    private ResetPasswordRequest resetPasswordRequest;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Doe", "john.doe@example.com", "password123", LocalDate.of(1990, 1, 1));
        testUser.setId(1L);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setVersion(1L);

        testUserResponse = new UserResponse(
                testUser.getId(),
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getBirthday(),
                testUser.getCreatedAt(),
                testUser.getUpdatedAt(),
                testUser.getVersion()
        );

        createRequest = new CreateUserRequest(
                "Jane", "Smith", "jane.smith@example.com", "password456", LocalDate.of(1995, 5, 15)
        );

        updateRequest = new UpdateUserRequest(
                "Jane", "Smith", "jane.smith@example.com", LocalDate.of(1995, 5, 15)
        );

        resetPasswordRequest = new ResetPasswordRequest("newPassword123");
    }

    @Test
    void getAllUsers_ShouldReturnPaginatedResponse() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll(0, 10)).thenReturn(users);
        when(userRepository.count()).thenReturn(1L);

        // Act
        PaginatedResponse response = userService.getAllUsers(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());

        verify(userRepository).findAll(0, 10);
        verify(userRepository).count();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        UserResponse result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testUser.getEmail(), result.getEmail());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getUserById(999L));
        verify(userRepository).findById(999L);
    }

    @Test
    void createUser_WhenValidRequest_ShouldCreateUser() {
        // Arrange
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.createUser(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getFirstName(), result.getFirstName());

        verify(userRepository).existsByEmail(createRequest.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailAlreadyExists_ShouldThrowBadRequestException() {
        // Arrange
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.createUser(createRequest));
        verify(userRepository).existsByEmail(createRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(false);
        when(userRepository.update(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.updateUser(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail(updateRequest.getEmail());
        verify(userRepository).update(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.updateUser(999L, updateRequest));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void updateUser_WhenEmailAlreadyExists_ShouldThrowBadRequestException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.updateUser(1L, updateRequest));
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail(updateRequest.getEmail());
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void resetPassword_WhenUserExists_ShouldResetPassword() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.update(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.resetPassword(1L, resetPasswordRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());

        verify(userRepository).findById(1L);
        verify(userRepository).update(any(User.class));
    }

    @Test
    void resetPassword_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.resetPassword(999L, resetPasswordRequest));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.deleteById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.deleteById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.deleteUser(999L));
        verify(userRepository).deleteById(999L);
    }
} 