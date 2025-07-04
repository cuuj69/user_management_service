package com.tech11.usermanagement.resource;

import com.tech11.usermanagement.data.ApiResponse;
import com.tech11.usermanagement.data.PaginatedResponse;
import com.tech11.usermanagement.dto.request.CreateUserRequest;
import com.tech11.usermanagement.dto.request.ResetPasswordRequest;
import com.tech11.usermanagement.dto.request.UpdateUserRequest;
import com.tech11.usermanagement.dto.response.UserResponse;
import com.tech11.usermanagement.services.UserService;
import com.tech11.usermanagement.dto.response.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PaginatedResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid Page Number",
                                            value = "{\"statusCode\": 400, \"message\": \"Page number must be non-negative\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Page Size",
                                            value = "{\"statusCode\": 400, \"message\": \"Page size must be between 1 and 100\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    )
                            }
                    )
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

        long startTime = System.currentTimeMillis();
        PaginatedResponse<UserResponse> paginatedData = userService.getAllUsers(firstName, lastName, email, page, size);
        long processingTime = System.currentTimeMillis() - startTime;
        
        // Create custom response with just the user list data
        ApiResponse<List<UserResponse>> response = new ApiResponse<>(
            200, 
            "Success (in " + processingTime + "ms)", 
            paginatedData.getData(), 
            processingTime
        );
        
        // Override the default pageData with actual pagination info
        ApiResponse.PageData pageData = new ApiResponse.PageData(
            page + 1, // Convert to 1-based
            size,
            paginatedData.getTotalPages(),
            paginatedData.getData().size(),
            (int) paginatedData.getTotalElements()
        );
        
        response.getData().setPageData(pageData);
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "User Not Found",
                                            value = "{\"statusCode\": 404, \"message\": \"User not found with id: 1234567890abcdef1234567890abcdef\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    )
                            }
                    )
            )
    })
    public Response getUserById(
            @Parameter(description = "User ID", example = "550e8400e29b41d4a716446655440000")
            @PathParam("id") String id) {

        long startTime = System.currentTimeMillis();
        UserResponse user = userService.getUserById(id);
        long processingTime = System.currentTimeMillis() - startTime;
        
        // For single record, return the data directly without pagination wrapper
        ApiResponse<UserResponse> response = new ApiResponse<>(
            200, 
            "Success (in " + processingTime + "ms)", 
            user
        );
        
        return Response.ok(response).build();
    }

    @POST
    @Operation(
            summary = "Create new user",
            description = "Create a new user with the provided information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation failed or email already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Email Already Exists",
                                            value = "{\"statusCode\": 400, \"message\": \"Email already exists\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Validation Errors",
                                            value = "{\"statusCode\": 400, \"message\": \"Validation failed\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": [{\"field\": \"email\", \"message\": \"Email must be a valid email address\", \"rejectedValue\": \"invalid-email\"}, {\"field\": \"firstName\", \"message\": \"First name is required\", \"rejectedValue\": \"\"}]}"
                                    )
                            }
                    )
            )
    })
    public Response createUser(
            @Parameter(description = "User creation request", required = true)
            @Valid CreateUserRequest request) {

        long startTime = System.currentTimeMillis();
        UserResponse createdUser = userService.createUser(request);
        long processingTime = System.currentTimeMillis() - startTime;
        
        // For single record, return the data directly without pagination wrapper
        ApiResponse<UserResponse> response = new ApiResponse<>(
            201, 
            "Success (in " + processingTime + "ms)", 
            createdUser
        );
        
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Update user",
            description = "Update an existing user's information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation failed or email already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Email Already Exists",
                                            value = "{\"statusCode\": 400, \"message\": \"Email already exists\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Validation Errors",
                                            value = "{\"statusCode\": 400, \"message\": \"Validation failed\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": [{\"field\": \"email\", \"message\": \"Email must be a valid email address\", \"rejectedValue\": \"invalid-email\"}]}"
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "User Not Found",
                                            value = "{\"statusCode\": 404, \"message\": \"User not found with id: 1234567890abcdef1234567890abcdef\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    )
                            }
                    )
            )
    })
    public Response updateUser(
            @Parameter(description = "User ID", example = "550e8400e29b41d4a716446655440000")
            @PathParam("id") String id,
            @Parameter(description = "User update request", required = true)
            @Valid UpdateUserRequest request) {

        long startTime = System.currentTimeMillis();
        UserResponse updatedUser = userService.updateUser(id, request);
        long processingTime = System.currentTimeMillis() - startTime;
        
        // For single record, return the data directly without pagination wrapper
        ApiResponse<UserResponse> response = new ApiResponse<>(
            200, 
            "Success (in " + processingTime + "ms)", 
            updatedUser
        );
        
        return Response.ok(response).build();
    }

    @PATCH
    @Path("/{id}/password")
    @Operation(
            summary = "Reset user password",
            description = "Reset the password for an existing user"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Password reset successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid password data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Password Validation Error",
                                            value = "{\"statusCode\": 400, \"message\": \"Password validation failed\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": [{\"field\": \"newPassword\", \"message\": \"Password must be at least 8 characters long\", \"rejectedValue\": \"123\"}]}"
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "User Not Found",
                                            value = "{\"statusCode\": 404, \"message\": \"User not found with id: 1234567890abcdef1234567890abcdef\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    )
                            }
                    )
            )
    })
    public Response resetPassword(
            @Parameter(description = "User ID", example = "550e8400e29b41d4a716446655440000")
            @PathParam("id") String id,
            @Parameter(description = "Password reset request", required = true)
            @Valid ResetPasswordRequest request) {

        long startTime = System.currentTimeMillis();
        UserResponse updatedUser = userService.resetPassword(id, request);
        long processingTime = System.currentTimeMillis() - startTime;
        
        // For single record, return the data directly without pagination wrapper
        ApiResponse<UserResponse> response = new ApiResponse<>(
            200, 
            "Success (in " + processingTime + "ms)", 
            updatedUser
        );
        
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Delete an existing user"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "User Not Found",
                                            value = "{\"statusCode\": 404, \"message\": \"User not found with id: 1234567890abcdef1234567890abcdef\", \"timestamp\": \"2025-07-04T12:00:00\", \"errors\": null}"
                                    )
                            }
                    )
            )
    })
    public Response deleteUser(
            @Parameter(description = "User ID", example = "550e8400e29b41d4a716446655440000")
            @PathParam("id") String id) {

        long startTime = System.currentTimeMillis();
        userService.deleteUser(id);
        long processingTime = System.currentTimeMillis() - startTime;
        
        // For delete operation, return 204 No Content as per REST standards
        return Response.noContent().build();
    }
} 