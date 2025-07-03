package com.tech11.usermanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application configuration for User Management Service.
 * Configures the application path and OpenAPI documentation.
 */
@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(
                title = "User Management API",
                version = "1.0.0",
                description = "RESTful API for managing user accounts",
                contact = @Contact(
                        name = "User Management API",
                        email = "mensahjefferson69@gmail.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:9080",
                        description = "Development Server"
                )
        }
)
public class UserManagementApplication extends Application {
    // JAX-RS will automatically discover and register all resources
} 