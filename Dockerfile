#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
#COPY src/main/resources/application.properties /app/config/application.properties
#EXPOSE 8080
#ENV SPRING_CONFIG_LOCATION = /app/config/application.properties
#ENTRYPOINT ["java", "-jar", "app.jar","--spring.config.location=/app/config/application.properties"]


# Use an official Maven image to build the project
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory in container
WORKDIR /app

# Copy pom.xml and download dependencies (improves caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JDK image for running the app
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR from the previous stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Copy application configuration
COPY src/main/resources/application.yml /app/config/application.yml

# Expose port
EXPOSE 8080

# Environment variable for Spring Boot to locate configuration file
ENV SPRING_CONFIG_LOCATION=/app/config/application.yml

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/app/config/application.yml"]