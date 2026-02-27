# ---- Build Stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy everything
COPY . .

# Build the JAR
RUN ./mvnw -q -DskipTests package

# ---- Run Stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]