FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app

# Copy maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execution permission to maven wrapper
RUN chmod +x mvnw

# Resolve dependencies (can be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/customer-service-1.0.jar app.jar

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
