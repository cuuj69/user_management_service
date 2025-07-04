# User Management Service - Tech11 Assessment

A RESTful user management service built with Java EE/Jakarta EE and Open Liberty, demonstrating modern enterprise Java development practices.

## Project Overview

This application implements a complete user management system with the following features:

- **Documentation**: Swagger/OpenAPI documentation
- **Testing**: Unit tests and integration tests
- **In-Memory Database**: H2 database for development and testing

## Architecture

The application follows a layered architecture pattern:

```
┌─────────────────┐
│   REST Layer    │  ← JAX-RS Resources
├─────────────────┤
│  Service Layer  │  ← Business Logic
├─────────────────┤
│   DAO Layer     │  ← Data Access
├─────────────────┤
│  Entity Layer   │  ← JPA Entities
└─────────────────┘
```

### Technology Stack

- **Java 17** - Latest LTS version
- **Jakarta EE 10** - Enterprise Java platform
- **Open Liberty 23.0.0.12** - Application server
- **JPA/Hibernate** - Object-relational mapping
- **H2 Database** - In-memory database
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Arquillian** - Integration testing
- **Swagger/OpenAPI** - API documentation
- **Maven** - Build tool

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Open Liberty (will be downloaded automatically by Maven)
- Docker and Docker Compose (for containerized deployment)

### Environment Configuration

The application uses multiple configuration files for different contexts:

#### For Local Development
```bash
# Copy example files
cp bootstrap.properties.example bootstrap.properties
cp server.env.example server.env

# Edit configuration as needed
```

#### For Docker Deployment
```bash
# Copy Docker environment file
cp docker.env.example .env

# Edit configuration as needed
```

#### Configuration Files
- **`bootstrap.properties`** - Liberty server bootstrap configuration
- **`server.env`** - Liberty server environment variables
- **`.env`** - Docker Compose environment variables
- **`META-INF/microprofile-config.properties`** - Application configuration

### Building the Application

```bash
# Clone the repository
git clone <repository-url>
cd tech11_assessment

# Build the application
mvn clean package
```

### Running the Application

#### Option 1: Local Development
```bash
# Start Open Liberty server
mvn liberty:dev

# The application will be available at:
# - Application: http://localhost:9080
# - Swagger UI: http://localhost:9080/openapi/ui
# - Health Check: http://localhost:9080/api/users/health
```

#### Option 2: Docker (Recommended)
```bash
# Build and prepare for Docker
./build-for-docker.sh

# Run with Docker Compose
docker-compose up --build

# The application will be available at:
# - Application: http://localhost:9080/user-management/api/users
# - Health Check: http://localhost:9080/user-management/api/users/health
```

#### Option 3: Production Docker
```bash
# Build and run production container
docker-compose -f docker-compose.prod.yml up -d

# View logs
docker-compose -f docker-compose.prod.yml logs -f

# Stop the application
docker-compose -f docker-compose.prod.yml down
```

### Running Tests

```bash
# Run unit tests only
mvn test

# Run integration tests
mvn verify

# Run all tests
mvn clean verify
```

## API Documentation

### Base URL
```
http://localhost:9080/api/users
```

### Endpoints

#### 1. Get All Users (Paginated)
```http
GET /api/users?page=0&size=10
```

**Response:**
```json
{
  "data": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "birthday": "1990-01-01",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null,
      "version": 1
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

#### 2. Get User by ID
```http
GET /api/users/{id}
```

#### 3. Create New User
```http
POST /api/users
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "birthday": "1990-01-01"
}
```

#### 4. Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "birthday": "1995-05-15"
}
```

#### 5. Reset Password
```http
PATCH /api/users/{id}/password
Content-Type: application/json

{
  "newPassword": "newSecurePassword123"
}
```

#### 6. Delete User
```http
DELETE /api/users/{id}
```

## Docker

### Docker Images

The application provides two Docker configurations:

#### Development Docker (`Dockerfile`)
- Multi-stage build for faster development cycles
- Includes all development tools and debugging capabilities
- Uses `docker-compose.yml` for orchestration

#### Production Docker (`Dockerfile.prod`)
- Optimized for production deployment
- Runs as non-root user for security
- Includes resource limits and security hardening
- Uses `docker-compose.prod.yml` for orchestration

### Docker Commands

```bash
# Build development image
docker build -t user-management-api .

# Build production image
docker build -f Dockerfile.prod -t user-management-api:prod .

# Run development container
docker-compose up -d

# Run production container
docker-compose -f docker-compose.prod.yml up -d

# View logs
docker-compose logs -f

# Stop containers
docker-compose down

# Remove all containers and images
docker-compose down --rmi all --volumes --remove-orphans
```

### Docker Features

- **Multi-stage builds** for optimized image sizes
- **Health checks** for container monitoring
- **Volume mounts** for logs and configuration
- **Resource limits** for production deployments
- **Security hardening** with non-root user execution
- **Network isolation** with custom Docker networks

## Testing

### Unit Tests
Unit tests cover the service layer with mocked dependencies:

```bash
mvn test
```

### Integration Tests
Integration tests use Arquillian to test the full application stack:

```bash
mvn verify
```

### Postman Collection
A comprehensive Postman collection is included for manual testing:

1. Import `User_Management_API.postman_collection.json` into Postman
2. Set the `baseUrl` variable to `http://localhost:9080`
3. Run the test scenarios

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/tech11/usermanagement/
│   │       ├── entity/
│   │       │   └── User.java
│   │       ├── data/
│   │       │   ├── ApiResponse.java
│   │       │   ├── PaginatedResponse.java
│   │       │   └── extensions/
│   │       ├── dto/
│   │       │   ├── request/
│   │       │   │   ├── CreateUserRequest.java
│   │       │   │   ├── UpdateUserRequest.java
│   │       │   │   └── ResetPasswordRequest.java
│   │       │   └── response/
│   │       │       └── UserResponse.java
│   │       ├── repository/
│   │       │   └── UserRepository.java
│   │       ├── service/
│   │       │   └── UserService.java
│   │       ├── resource/
│   │       │   └── UserResource.java
│   │       ├── validators/
│   │       │   ├── CreateUserRequestValidator.java
│   │       │   ├── UpdateUserRequestValidator.java
│   │       │   └── ResetPasswordRequestValidator.java
│   │       └── UserManagementApplication.java
│   ├── resources/
│   │   └── META-INF/
│   │       ├── persistence.xml
│   │       └── microprofile-config.properties
│   ├── liberty/
│   │   └── config/
│   │       └── server.xml
│   └── webapp/
│       └── WEB-INF/
│           └── beans.xml
├── test/
│   ├── java/
│   │   └── com/tech11/usermanagement/
│   │       ├── services/
│   │       │   └── UserServiceTest.java
│   │       └── resource/
│   │           └── UserResourceIntegrationTest.java
│   └── resources/
│       └── arquillian.xml
```

### Key Changes from Original Structure:

- **Removed DAO layer** - Replaced with JPA Repository pattern
- **Restructured DTOs** - Separated into `request/` and `response/` packages
- **Added Data layer** - `ApiResponse` and `PaginatedResponse` moved to `data/` package
- **Added Validators** - Custom validation classes for request DTOs
- **Added Configuration** - `microprofile-config.properties` for application settings
- **Updated Test structure** - Service tests moved to `services/` package

## 🔧 Configuration

### Open Liberty Server Configuration
The server is configured in `src/main/liberty/config/server.xml` with:

- Jakarta EE 10 features enabled
- H2 in-memory database
- JPA persistence unit
- HTTP endpoints on ports 9080/9443

### JPA Configuration
Database configuration is in `src/main/resources/META-INF/persistence.xml`:

- H2 in-memory database
- Hibernate as JPA provider
- Automatic schema generation

### Technical Services Implemented

1. **User Registration Service** - Complete user creation with validation
2. **User Profile Management** - Update user information
3. **Password Reset Service** - Secure password reset functionality
4. **User Listing Service** - Paginated user listing
5. **User Account Disabling Service** - Safe user account disabling 

### Additional Services (Stubs)

The following services could be implemented for a complete user management system:

1. **Email Verification Service** - Email confirmation for new accounts
2. **Account Lockout Service** - Security for failed login attempts
3. **User Activity Logging** - Audit trail for user actions
4. **Bulk User Operations** - Import/export functionality
5. **User Search Service** - Advanced search and filtering

## 🚀 Deployment

### Development
```bash
mvn liberty:dev
```

### Production Build
```bash
mvn clean package
mvn liberty:create liberty:install-feature liberty:deploy liberty:start
```

## 📝 Code Quality

This project demonstrates:

- **Clean Architecture**: Separation of concerns
- **SOLID Principles**: Single responsibility, dependency injection
- **Test-Driven Development**: Comprehensive test coverage
- **Documentation**: Inline documentation and API docs
- **Error Handling**: Proper exception handling
- **Validation**: Input validation at multiple layers

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

William Jefferson Mensah