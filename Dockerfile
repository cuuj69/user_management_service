# Multi-stage build for Liberty application
FROM maven:3.9.6-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openliberty/open-liberty:25.0.0.6-kernel-java17-openj9-ubi

# Set working directory
WORKDIR /opt/ol/wlp

# Copy the built WAR file
COPY --from=build /app/target/user-management.war /opt/ol/wlp/usr/servers/defaultServer/apps/

# Copy server configuration
COPY src/main/liberty/config/server.xml /opt/ol/wlp/usr/servers/defaultServer/

# Copy H2 database driver
COPY --from=build /root/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar /opt/ol/wlp/usr/shared/resources/

# Create shared resources directory
RUN mkdir -p /opt/ol/wlp/usr/shared/resources

# Expose ports
EXPOSE 9080 9443

# Set health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:9080/health || exit 1

# Start Liberty server
CMD ["/opt/ol/wlp/bin/server", "run", "defaultServer"] 