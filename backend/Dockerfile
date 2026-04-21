# ---- Build stage: compile the Spring Boot jar with Maven + JDK 17 --------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Dependencies first so the layer stays cached across code-only edits
COPY pom.xml .
RUN mvn -q -B -e -DskipTests dependency:go-offline

# Sources and package
COPY src ./src
RUN mvn -q -B -e -DskipTests package

# ---- Runtime stage: slim JRE image, non-root user, tiny footprint --------------
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
