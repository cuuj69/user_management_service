# User Management Service - Tech11 Assessment

A comprehensive RESTful user management service built with Jakarta EE 10 and Open Liberty, featuring modern enterprise Java development practices, comprehensive error handling, logging, and scalable architecture.

## 🎯 Project Overview

This application implements a complete user management system with enterprise-grade features:

- **🔐 Secure User Management**: CRUD operations with UUID-based IDs and validation
- **📊 Standardized API Responses**: Consistent JSON format with pagination support
- **🛡️ Comprehensive Validation**: Bean validation and business logic validation
- **📝 Detailed Error Handling**: Structured error responses with validation messages
- **📋 Comprehensive Logging**: Multi-level logging with rotation and management tools
- **🔧 Service Stubs**: Ready-to-implement notification, audit, and email services
- **🎭 Actor System**: Asynchronous message processing for scalability
- **🚀 CI/CD Pipeline**: GitHub Actions with automated testing and deployment
- **📚 API Documentation**: Swagger/OpenAPI with detailed examples
- **🧪 Testing**: Unit tests with comprehensive coverage
- **🐳 Containerization**: Docker support for easy deployment

## 🏗️ Architecture

The application follows a modern layered architecture with actor-based asynchronous processing:

```
┌─────────────────────────────────────────────────────────────┐
│                    REST API Layer                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   UserResource  │  │  ErrorResponse  │  │  ApiResponse │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                   Service Layer                             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   UserService   │  │  Notification   │  │  AuditService│ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                  Actor System                               │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   UserActor     │  │  SystemActor    │  │  MessageBus  │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                   Data Layer                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │ UserRepository  │  │     User        │  │ Validators   │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                  Infrastructure                             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   H2 Database   │  │  Open Liberty   │  │   Logging    │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Key Architectural Features

- **🔄 Asynchronous Processing**: Actor system for non-blocking operations
- **📨 Message-Driven**: Event-based communication between components
- **🛡️ Validation Layers**: Multiple validation levels (Bean, Business, Custom)
- **📊 Response Standardization**: Consistent API response format
- **🔍 Comprehensive Logging**: Multi-level logging with rotation
- **⚡ Error Handling**: Structured error responses with detailed messages

### Technology Stack

#### Core Technologies
- **Java 17** - Latest LTS version with modern features
- **Jakarta EE 10** - Enterprise Java platform with CDI, JAX-RS, JPA
- **Open Liberty 23.0.0.12** - Lightweight, fast application server
- **JPA/EclipseLink** - Object-relational mapping with UUID support
- **H2 Database** - In-memory database with automatic schema creation

#### Development & Testing
- **JUnit 5** - Modern unit testing framework
- **Mockito** - Mocking framework for isolated testing
- **Maven** - Build tool with dependency management
- **Swagger/OpenAPI 3.1** - Interactive API documentation

#### Validation & Security
- **Bean Validation 3.0** - Declarative validation with custom constraints
- **OWASP Dependency Check** - Security vulnerability scanning
- **UUID-based IDs** - Secure, scalable identifier system

#### Logging & Monitoring
- **Java Logging** - Multi-level logging with rotation
- **Custom Log Management** - Script-based log handling
- **Structured Error Responses** - Consistent error handling

#### Future-Ready Components
- **Actor System** - Asynchronous message processing
- **Service Stubs** - Notification, Audit, Email services
- **Message Bus** - Event-driven communication

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **Docker** and Docker Compose (for containerized deployment)
- **Git** for version control

### Quick Start

```bash
# Clone the repository
git clone <repository-url>
cd tech11_assessment

# Build and run locally
mvn liberty:dev

# Or use Docker
docker-compose up --build
```

### Application URLs

- **Application**: http://localhost:9080/user-management/api/users
- **Swagger UI**: http://localhost:9080/openapi/ui
- **Health Check**: http://localhost:9080/user-management/api/users/health

### Environment Configuration

Copy the example environment file and configure your settings:

```bash
cp server.env.example server.env
```

Edit `server.env` to configure:
- **Database Settings**: H2 connection parameters
- **Server Settings**: Liberty server configuration
- **Logging**: Log levels and rotation settings
- **Security**: Application security settings

### Building and Running

#### Local Development
```bash
# Build the application
mvn clean package

# Start Open Liberty server
mvn liberty:dev

# The application will be available at:
# - Application: http://localhost:9080/user-management/api/users
# - Swagger UI: http://localhost:9080/openapi/ui
# - Health Check: http://localhost:9080/user-management/api/users/health
```

#### Docker Deployment
```bash
# Build and prepare for Docker
./build-for-docker.sh

# Run with Docker Compose
docker-compose up --build

# View logs
docker-compose logs -f

# Stop the application
docker-compose down
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

## Logging and Monitoring

### Log Configuration

The application uses Liberty's built-in logging with the following configuration:

- **Log Level**: INFO (configurable in `src/main/liberty/config/server.xml`)
- **Log Rotation**: 10MB max file size, keep 10 files
- **Console Output**: Enabled for development
- **Log Location**: `target/liberty/wlp/usr/servers/defaultServer/logs/`

### Log Management Script

A convenient script is provided to manage logs:

```bash
# Show help
./logs.sh help

# Follow logs in real-time
./logs.sh tail

# Show last 50 log messages
./logs.sh messages

# Show only error messages
./logs.sh errors

# Show only warning messages
./logs.sh warnings

# Show application-specific logs (our service logs)
./logs.sh app

# Show log file sizes
./logs.sh size

# Clear all log files
./logs.sh clear
```

### Log Levels

- **INFO**: Method entry, successful operations, field updates
- **WARNING**: Validation failures, not found scenarios, bad requests
- **SEVERE**: Unexpected exceptions with full stack traces

### Example Log Output

```
[INFO] Creating new user with email: john.doe@example.com
[INFO] Successfully created user with ID: e42e49eeb30a4944a501bb7e430c6df7
[INFO] Updating user with ID: e42e49eeb30a4944a501bb7e430c6df7
[INFO] Updating firstName for user: e42e49eeb30a4944a501bb7e430c6df7
[WARNING] User not found with ID: invalid-id
[SEVERE] Error creating user: Database connection failed
```

### Log Retention

- **Development**: Logs are rotated automatically (10MB max, 10 files)
- **Production**: Consider implementing external log aggregation (ELK stack, Splunk, etc.)
- **Cleanup**: Use `./logs.sh clear` to clean up old logs when needed

## CI/CD Pipeline

### GitHub Actions Workflows

The project includes comprehensive CI/CD pipelines:

#### 1. **Quick PR Validation** (`.github/workflows/pr-validation.yml`)
- Runs on every pull request
- Quick feedback (15-minute timeout)
- Validates: POM, compilation, unit tests, code formatting, dependencies, security scan
- Comments PR with results

#### 2. **Full CI/CD Pipeline** (`.github/workflows/ci.yml`)
- Comprehensive validation on PRs and pushes to main/develop
- Includes: code quality, unit tests, integration tests, security scan, build, Docker build
- Generates test coverage and security reports
- Creates artifacts for deployment

#### 3. **Deployment Pipeline** (`.github/workflows/deploy.yml`)
- Deploys to staging and production environments
- Triggered by tags (v*) or manual workflow dispatch
- Creates GitHub releases automatically
- Includes health checks and notifications

### Branch Protection

The repository uses branch protection rules:

- **Main Branch**: Requires 2 approvals, all CI checks, signed commits
- **Develop Branch**: Requires 1 approval, core CI checks
- **Feature Branches**: Basic validation, flexible rules
- **Hotfix Branches**: Strict validation, 2 approvals required

### Code Ownership

- **Backend Team**: Java code, services, repositories
- **DevOps Team**: Configuration, Docker, GitHub Actions
- **Security Team**: Security-related configurations
- **Docs Team**: Documentation and README files

### Required Secrets

Set up these secrets in your GitHub repository:

```bash
# For SonarQube analysis
SONAR_TOKEN=your_sonar_token

# For container registry
REGISTRY_URL=your_registry_url
REGISTRY_USERNAME=your_registry_username
REGISTRY_PASSWORD=your_registry_password

# For deployment
DEPLOYMENT_KEY=your_deployment_key
```

### Local Development

To run the same checks locally:

```bash
# Validate POM
mvn validate

# Run tests
mvn test

# Check code formatting
mvn spotless:check

# Security scan
mvn org.owasp:dependency-check-maven:check

# Build application
mvn clean package
```

### Pull Request Guidelines

1. **Use the PR template** - Fill out all required sections
2. **Follow conventional commits** - Use proper commit message format
3. **Ensure all checks pass** - CI/CD pipeline must succeed
4. **Get required approvals** - Follow code ownership rules
5. **Update documentation** - Keep README and docs current

## 📚 API Documentation

### Base URL
```
http://localhost:9080/user-management/api/users
```

### Standardized Response Format

All API responses follow a consistent format:

#### Success Response (Single Record)
```json
{
  "statusCode": 200,
  "message": "Success (in 15ms)",
  "data": {
    "id": "a9b81789284a4551a536992879d08871",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "birthday": "1990-01-01",
    "createdAt": "2024-01-15T10:30:00",
    "version": 1
  }
}
```

#### Success Response (List with Pagination)
```json
{
  "statusCode": 200,
  "message": "Success (in 25ms)",
  "data": {
    "data": [
      {
        "id": "a9b81789284a4551a536992879d08871",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "birthday": "1990-01-01",
        "createdAt": "2024-01-15T10:30:00",
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
}
```

#### Error Response
```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email must be a valid email address"
    },
    {
      "field": "password",
      "message": "Password must be at least 8 characters long"
    }
  ]
}
```

### Endpoints

#### 1. Get All Users (Paginated)
```http
GET /user-management/api/users?page=0&size=10
```

#### 2. Get User by ID
```http
GET /user-management/api/users/{id}
```

#### 3. Create New User
```http
POST /user-management/api/users
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
PUT /user-management/api/users/{id}
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
PATCH /user-management/api/users/{id}/password
Content-Type: application/json

{
  "newPassword": "newSecurePassword123"
}
```

#### 6. Delete User
```http
DELETE /user-management/api/users/{id}
```
**Returns**: HTTP 204 No Content

## 🐳 Docker

### Docker Configuration

The application provides Docker support for easy deployment:

#### Development Docker
- Multi-stage build for faster development cycles
- Includes all development tools and debugging capabilities
- Uses `docker-compose.yml` for orchestration

### Docker Commands

```bash
# Build and run development container
docker-compose up --build

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
- **Network isolation** with custom Docker networks
- **Environment configuration** via `server.env`

## 🧪 Testing

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
2. Set the `baseUrl` variable to `http://localhost:9080/user-management`
3. Run the test scenarios

### Test Coverage
- **Service Layer**: Comprehensive unit tests with mocked dependencies
- **Validation**: Tests for all validation scenarios
- **Error Handling**: Tests for exception scenarios
- **UUID Handling**: Tests for UUID conversion and validation

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/tech11/usermanagement/
│   │       ├── actors/
│   │       │   ├── MessageBus.java
│   │       │   ├── SystemActor.java
│   │       │   ├── UserActor.java
│   │       │   └── messages/
│   │       │       ├── SystemMessages.java
│   │       │       └── UserMessages.java
│   │       ├── config/
│   │       │   ├── GlobalExceptionMapper.java
│   │       │   └── ValidationExceptionMapper.java
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
│   │       │       ├── ErrorResponse.java
│   │       │       └── UserResponse.java
│   │       ├── entity/
│   │       │   └── User.java
│   │       ├── repository/
│   │       │   └── UserRepository.java
│   │       ├── resource/
│   │       │   └── UserResource.java
│   │       ├── services/
│   │       │   ├── AuditService.java
│   │       │   ├── EmailService.java
│   │       │   ├── NotificationService.java
│   │       │   └── UserService.java
│   │       ├── validators/
│   │       │   ├── CreateUserRequestValidator.java
│   │       │   ├── ResetPasswordRequestValidator.java
│   │       │   └── UpdateUserRequestValidator.java
│   │       └── UserManagementApplication.java
│   ├── liberty/
│   │   └── config/
│   │       └── server.xml
│   ├── resources/
│   │   └── META-INF/
│   │       ├── microprofile-config.properties
│   │       └── persistence.xml
│   └── webapp/
│       └── WEB-INF/
│           └── beans.xml
├── test/
│   └── java/
│       └── com/tech11/usermanagement/
│           └── services/
│               └── UserServiceTest.java
```

### Key Architectural Components:

- **🎭 Actor System**: Asynchronous message processing with `UserActor` and `SystemActor`
- **📨 Message Bus**: Event-driven communication between components
- **🛡️ Validation**: Multi-layer validation with custom validators
- **📊 Response Standardization**: Consistent API response format with `ApiResponse`
- **🔍 Error Handling**: Structured error responses with `ErrorResponse`
- **📋 Service Layer**: Core business logic with service stubs for future expansion
- **🗄️ Data Layer**: JPA repository pattern with UUID-based entities

## 🔧 Configuration

### Open Liberty Server Configuration
The server is configured in `src/main/liberty/config/server.xml` with:

- Jakarta EE 10 features enabled
- H2 in-memory database with automatic schema creation
- JPA persistence unit with EclipseLink
- HTTP endpoints on ports 9080/9443
- Logging configuration with rotation

### JPA Configuration
Database configuration is in `src/main/resources/META-INF/persistence.xml`:

- H2 in-memory database with UUID support
- EclipseLink as JPA provider
- Automatic schema generation
- UUID-based ID generation strategy

### Application Configuration
Application settings in `src/main/resources/META-INF/microprofile-config.properties`:

- Database connection parameters
- Logging levels
- Application-specific settings

## 🎯 Implemented Features

### Core User Management
1. **User Registration** - Complete user creation with comprehensive validation
2. **User Profile Management** - Update user information with field-level tracking
3. **Password Reset** - Secure password reset functionality
4. **User Listing** - Paginated user listing with sorting
5. **User Deletion** - Safe user account removal (HTTP 204)

### Advanced Features
1. **UUID-based IDs** - Secure, scalable identifier system
2. **Standardized API Responses** - Consistent JSON format with processing time
3. **Comprehensive Validation** - Bean validation + custom business validation
4. **Detailed Error Handling** - Structured error responses with field-level messages
5. **Multi-level Logging** - INFO, WARNING, SEVERE levels with rotation
6. **Actor System** - Asynchronous message processing for scalability

### Service Stubs (Ready for Implementation)
1. **NotificationService** - Email/SMS notifications
2. **AuditService** - User activity logging and audit trails
3. **EmailService** - Email delivery and management
4. **Message Bus** - Event-driven communication between services

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

### Docker Deployment
```bash
./build-for-docker.sh
docker-compose up --build
```

## 📝 Code Quality & Best Practices

This project demonstrates enterprise-grade development practices:

- **🏗️ Clean Architecture**: Clear separation of concerns with layered design
- **🔒 SOLID Principles**: Single responsibility, dependency injection, interface segregation
- **🧪 Test-Driven Development**: Comprehensive unit test coverage
- **📚 Documentation**: Inline documentation and interactive API docs
- **🛡️ Error Handling**: Proper exception handling with structured responses
- **✅ Validation**: Multi-layer validation (Bean, Business, Custom)
- **📋 Logging**: Comprehensive logging with proper levels and rotation
- **🔐 Security**: UUID-based IDs, input validation, secure configurations
- **⚡ Performance**: Asynchronous processing, efficient database queries
- **🐳 Containerization**: Docker support for easy deployment

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

William Jefferson Mensah