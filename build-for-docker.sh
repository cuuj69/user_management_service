#!/bin/bash

# Build the application
echo "Building application..."
mvn clean package -DskipTests

# Create dependency directory
mkdir -p target/dependency

# Copy H2 driver to dependency directory
echo "Copying H2 driver..."
cp ~/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar target/dependency/

echo "Build complete! You can now run: docker-compose up --build" 