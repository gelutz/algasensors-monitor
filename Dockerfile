# Build stage
FROM gradle:8.11-jdk21 AS builder
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
COPY src src
RUN gradle clean bootJar -x test

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/monitor-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar", "--seed"]
# ENTRYPOINT ["java", "-jar", "app.jar"]
