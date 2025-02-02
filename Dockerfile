FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Install curl for the health check (since it's not included in slim images)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Use build arguments for flexibility (ensure JAR path is correct)
ARG JAR_FILE=target/MyPizza-0.0.1-SNAPSHOT.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the port the application will run on
EXPOSE 8443

# Add memory optimization flags and start the application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

# Optional health check (ensure the endpoint exists)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl --fail http://localhost:8443/actuator/health || exit 1
