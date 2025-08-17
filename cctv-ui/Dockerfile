# Multi-stage build for cctv-ui using the latest GraalVM Community image
# Build stage: compile the application in production mode
FROM ghcr.io/graalvm/graalvm-community:latest AS build

WORKDIR /workspace

# Copy Maven Wrapper and pom first to leverage Docker layer caching
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml pom.xml

# Ensure wrapper is executable
RUN chmod +x mvnw

# Pre-fetch dependencies to improve build caching
RUN ./mvnw -B -Pproduction -DskipTests dependency:go-offline

# Copy the application source
COPY src src

# Build the application in production mode (Vaadin frontend included)
RUN ./mvnw -B -Pproduction -DskipTests package

# Runtime stage: run the built application
FROM ghcr.io/graalvm/graalvm-community:latest AS runtime

WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
