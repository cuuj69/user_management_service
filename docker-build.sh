#!/bin/bash

# Build and run User Management API with Docker

echo "Building User Management API Docker image..."

# Build the Docker image
docker build -t user-management-api .

if [ $? -eq 0 ]; then
    echo "Docker image built successfully!"
    echo ""
    echo "Starting the application with docker-compose..."
    
    # Start the application
    docker-compose up -d
    
    if [ $? -eq 0 ]; then
        echo "Application started successfully!"
        echo ""
        echo "Service Information:"
        echo "   - API Base URL: http://localhost:9080/api/users"
        echo "   - Health Check: http://localhost:9080/api/users/health"
        echo "   - OpenAPI UI: http://localhost:9080/openapi/ui"
        echo ""
        echo "To view logs: docker-compose logs -f"
        echo "To stop: docker-compose down"
    else
        echo "Failed to start the application"
        exit 1
    fi
else
    echo "Failed to build Docker image"
    exit 1
fi 