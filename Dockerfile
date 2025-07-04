# Use the official Open Liberty image
FROM openliberty/open-liberty:25.0.0.6-kernel-java17-openj9-ubi

# Set working directory
WORKDIR /opt/ol/wlp

# Copy the application WAR file
COPY target/user-management.war /opt/ol/wlp/usr/servers/defaultServer/apps/

# Copy server configuration
COPY src/main/liberty/config/server.xml /opt/ol/wlp/usr/servers/defaultServer/

# Copy H2 database driver to shared resources
COPY target/dependency/h2-2.2.224.jar /opt/ol/wlp/usr/shared/resources/

# Expose ports
EXPOSE 9080 9443

# Set health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:9080/health || exit 1

# Start Liberty server
CMD ["/opt/ol/wlp/bin/server", "run", "defaultServer"] 