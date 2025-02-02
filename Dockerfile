FROM openjdk:17-jdk-slim
WORKDIR /app

# Use build arguments for flexibility
ARG JAR_FILE=target/MyPizza-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8443

# Add memory optimization flags
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

# Optional health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl --fail http://localhost:8443/actuator/health || exit 1
