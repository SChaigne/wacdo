FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY target/generated-test-sources/test-annotations .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
