package com.tech11.usermanagement.resource;

import com.tech11.usermanagement.dto.request.CreateUserRequest;
import com.tech11.usermanagement.dto.request.ResetPasswordRequest;
import com.tech11.usermanagement.dto.request.UpdateUserRequest;
import com.tech11.usermanagement.dto.response.UserResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserResourceIntegrationTest {

    private Client client;
    private String apiUrl = "http://localhost:9080/api/users";

    @BeforeEach
    void setUp() {
        client = ClientBuilder.newClient();
    }

    @AfterEach
    void tearDown() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    void testCreateAndGetUser() {
        // Create user
        CreateUserRequest createRequest = new CreateUserRequest(
                "John", "Doe", "john.doe@test.com", "password123", LocalDate.of(1990, 1, 1)
        );

        Response createResponse = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(createRequest, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), createResponse.getStatus());
        UserResponse createdUser = createResponse.readEntity(UserResponse.class);
        assertNotNull(createdUser.getId());
        assertEquals("John", createdUser.getFirstName());
        assertEquals("john.doe@test.com", createdUser.getEmail());

        // Get user by ID
        Response getResponse = client.target(apiUrl + "/" + createdUser.getId())
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), getResponse.getStatus());
        UserResponse retrievedUser = getResponse.readEntity(UserResponse.class);
        assertEquals(createdUser.getId(), retrievedUser.getId());
        assertEquals(createdUser.getFirstName(), retrievedUser.getFirstName());
    }

    @Test
    void testGetAllUsers() {
        Response response = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        // Response should be a paginated list
        assertNotNull(response.readEntity(String.class));
    }

    @Test
    void testUpdateUser() {
        // First create a user
        CreateUserRequest createRequest = new CreateUserRequest(
                "Jane", "Smith", "jane.smith@test.com", "password456", LocalDate.of(1995, 5, 15)
        );

        Response createResponse = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(createRequest, MediaType.APPLICATION_JSON));

        UserResponse createdUser = createResponse.readEntity(UserResponse.class);

        // Update the user
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "Jane", "Johnson", "jane.johnson@test.com", LocalDate.of(1995, 5, 15)
        );

        Response updateResponse = client.target(apiUrl + "/" + createdUser.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updateRequest, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), updateResponse.getStatus());
        UserResponse updatedUser = updateResponse.readEntity(UserResponse.class);
        assertEquals("Johnson", updatedUser.getLastName());
        assertEquals("jane.johnson@test.com", updatedUser.getEmail());
    }

    @Test
    void testResetPassword() {
        // First create a user
        CreateUserRequest createRequest = new CreateUserRequest(
                "Bob", "Wilson", "bob.wilson@test.com", "password789", LocalDate.of(1985, 10, 20)
        );

        Response createResponse = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(createRequest, MediaType.APPLICATION_JSON));

        UserResponse createdUser = createResponse.readEntity(UserResponse.class);

        // Reset password
        ResetPasswordRequest resetRequest = new ResetPasswordRequest("newPassword123");

        Response resetResponse = client.target(apiUrl + "/" + createdUser.getId() + "/password")
                .request(MediaType.APPLICATION_JSON)
                .method("PATCH", Entity.entity(resetRequest, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), resetResponse.getStatus());
        UserResponse updatedUser = resetResponse.readEntity(UserResponse.class);
        assertEquals(createdUser.getId(), updatedUser.getId());
    }

    @Test
    void testDeleteUser() {
        // First create a user
        CreateUserRequest createRequest = new CreateUserRequest(
                "Alice", "Brown", "alice.brown@test.com", "password101", LocalDate.of(1992, 3, 8)
        );

        Response createResponse = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(createRequest, MediaType.APPLICATION_JSON));

        UserResponse createdUser = createResponse.readEntity(UserResponse.class);

        // Delete the user
        Response deleteResponse = client.target(apiUrl + "/" + createdUser.getId())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.getStatus());

        // Verify user is deleted
        Response getResponse = client.target(apiUrl + "/" + createdUser.getId())
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getResponse.getStatus());
    }

    @Test
    void testGetUserNotFound() {
        Response response = client.target(apiUrl + "/999")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        CreateUserRequest createRequest = new CreateUserRequest(
                "John", "Doe", "duplicate@test.com", "password123", LocalDate.of(1990, 1, 1)
        );

        // Create first user
        Response createResponse1 = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(createRequest, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), createResponse1.getStatus());

        // Try to create second user with same email
        Response createResponse2 = client.target(apiUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(createRequest, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), createResponse2.getStatus());
    }
} 