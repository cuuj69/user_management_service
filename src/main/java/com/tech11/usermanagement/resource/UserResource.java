package com.tech11.usermanagement.resource;

import com.tech11.usermanagement.data.PaginatedResponse;
import com.tech11.usermanagement.dto.request.CreateUserRequest;
import com.tech11.usermanagement.dto.request.ResetPasswordRequest;
import com.tech11.usermanagement.dto.request.UpdateUserRequest;
import com.tech11.usermanagement.dto.response.UserResponse;
import com.tech11.usermanagement.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;

/**
 * REST resource for user management operations.
 * Provides endpoints for CRUD operations on users.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Management", description = "Operations for managing users")
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    @Path("/health")
    @Operation(
            summary = "Health check",
            description = "Simple health check endpoint for monitoring"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service is healthy"
            )
    })
    public Response healthCheck() {
        return Response.ok().entity("{\"status\": \"healthy\", \"service\": \"user-management\"}").build();
    }



    @GET
    @Operation(
            summary = "Get all users",
            description = "Retrieve a paginated list of users with optional search filters. You can search by firstName, lastName, or email using partial matching. Only one filter can be used at a time."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PaginatedResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters"
            )
    })
    public Response getAllUsers(
            @Parameter(description = "First name filter (partial match)", example = "John")
            @QueryParam("firstName") String firstName,
            @Parameter(description = "Last name filter (partial match)", example = "Doe")
            @QueryParam("lastName") String lastName,
            @Parameter(description = "Email filter (partial match)", example = "john@")
            @QueryParam("email") String email,
            @Parameter(description = "Page number (0-based)", example = "0")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Page size", example = "10")
            @QueryParam("size") @DefaultValue("10") int size) {

        PaginatedResponse<UserResponse> response = userService.getAllUsers(firstName, lastName, email, page, size);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public Response getUserById(
            @Parameter(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathParam("id") UUID id) {

        UserResponse user = userService.getUserById(id);
        return Response.ok(user).build();
    }

    @POST
    @Operation(
            summary = "Create new user",
            description = "Create a new user with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or email already exists"
            )
    })
    public Response createUser(
            @Parameter(description = "User creation request", required = true)
            @Valid CreateUserRequest request) {

        UserResponse createdUser = userService.createUser(request);
        return Response.status(Response.Status.CREATED)
                .entity(createdUser)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Update user",
            description = "Update an existing user's information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or email already exists"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public Response updateUser(
            @Parameter(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathParam("id") UUID id,
            @Parameter(description = "User update request", required = true)
            @Valid UpdateUserRequest request) {

        UserResponse updatedUser = userService.updateUser(id, request);
        return Response.ok(updatedUser).build();
    }

    @PATCH
    @Path("/{id}/password")
    @Operation(
            summary = "Reset user password",
            description = "Reset the password for an existing user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid password data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public Response resetPassword(
            @Parameter(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathParam("id") UUID id,
            @Parameter(description = "Password reset request", required = true)
            @Valid ResetPasswordRequest request) {

        UserResponse updatedUser = userService.resetPassword(id, request);
        return Response.ok(updatedUser).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Delete an existing user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public Response deleteUser(
            @Parameter(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathParam("id") UUID id) {

        userService.deleteUser(id);
        return Response.noContent().build();
    }
} 