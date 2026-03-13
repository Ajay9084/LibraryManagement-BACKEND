# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first for dependency caching
COPY pom.xml .

RUN mvn -B -q -e -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render uses dynamic port but expose default
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","app.jar"]