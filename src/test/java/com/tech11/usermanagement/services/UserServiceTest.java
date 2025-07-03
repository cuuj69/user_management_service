package com.tech11.usermanagement.services;

import com.tech11.usermanagement.repository.UserRepository;
import com.tech11.usermanagement.service.UserService;
import com.tech11.usermanagement.validators.CreateUserRequestValidator;
import com.tech11.usermanagement.validators.UpdateUserRequestValidator;
import com.tech11.usermanagement.validators.ResetPasswordRequestValidator;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreateUserRequestValidator createUserValidator;

    @Mock
    private UpdateUserRequestValidator updateUserValidator;

    @Mock
    private ResetPasswordRequestValidator resetPasswordValidator;

    @InjectMocks
    private UserService userService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final UUID NON_EXISTENT_USER_ID = UUID.randomUUID();

    private User testUser;
    private UserResponse testUserResponse;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;
    private ResetPasswordRequest resetPasswordRequest;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Doe", "john.doe@example.com", "password123", LocalDate.of(1990, 1, 1));
        testUser.setId(TEST_USER_ID);
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
        PaginatedResponse<UserResponse> response = userService.getAllUsers(null, null, null, 0, 10);

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
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));

        // Act
        UserResponse result = userService.getUserById(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testUser.getEmail(), result.getEmail());

        verify(userRepository).findById(TEST_USER_ID);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getUserById(NON_EXISTENT_USER_ID));
        verify(userRepository).findById(NON_EXISTENT_USER_ID);
    }

    @Test
    void createUser_WhenValidRequest_ShouldCreateUser() {
        // Arrange
        doNothing().when(createUserValidator).validate(createRequest);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.createUser(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getFirstName(), result.getFirstName());

        verify(createUserValidator).validate(createRequest);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailAlreadyExists_ShouldThrowBadRequestException() {
        // Arrange
        doThrow(new BadRequestException("Email already exists")).when(createUserValidator).validate(createRequest);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.createUser(createRequest));
        verify(createUserValidator).validate(createRequest);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Arrange
        doNothing().when(updateUserValidator).validate(TEST_USER_ID, updateRequest);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.update(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.updateUser(TEST_USER_ID, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());

        verify(updateUserValidator).validate(TEST_USER_ID, updateRequest);
        verify(userRepository).findById(TEST_USER_ID);
        verify(userRepository).update(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.updateUser(NON_EXISTENT_USER_ID, updateRequest));
        verify(userRepository).findById(NON_EXISTENT_USER_ID);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void updateUser_WhenEmailAlreadyExists_ShouldThrowBadRequestException() {
        // Arrange
        doThrow(new BadRequestException("Email already exists")).when(updateUserValidator).validate(TEST_USER_ID, updateRequest);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.updateUser(TEST_USER_ID, updateRequest));
        verify(updateUserValidator).validate(TEST_USER_ID, updateRequest);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void resetPassword_WhenUserExists_ShouldResetPassword() {
        // Arrange
        doNothing().when(resetPasswordValidator).validate(resetPasswordRequest);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.update(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.resetPassword(TEST_USER_ID, resetPasswordRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());

        verify(userRepository).findById(TEST_USER_ID);
        verify(userRepository).update(any(User.class));
    }

    @Test
    void resetPassword_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.resetPassword(NON_EXISTENT_USER_ID, resetPasswordRequest));
        verify(userRepository).findById(NON_EXISTENT_USER_ID);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.deleteById(TEST_USER_ID)).thenReturn(true);

        // Act
        userService.deleteUser(TEST_USER_ID);

        // Assert
        verify(userRepository).deleteById(TEST_USER_ID);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(userRepository.deleteById(NON_EXISTENT_USER_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.deleteUser(NON_EXISTENT_USER_ID));
        verify(userRepository).deleteById(NON_EXISTENT_USER_ID);
    }
} 