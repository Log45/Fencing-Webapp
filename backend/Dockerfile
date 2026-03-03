# ---------- Build stage ----------
FROM maven:4.0.0-eclipse-temurin-25 AS build

WORKDIR /app

# Copy Maven wrapper + pom first (better layer caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies only
RUN ./mvnw dependency:go-offline -B

# Copy source and build
COPY src src
RUN ./mvnw clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the Spring Boot fat jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]