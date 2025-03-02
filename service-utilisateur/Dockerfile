FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/service-utilisateur-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/service-utilisateur-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
