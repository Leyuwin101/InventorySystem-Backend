FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first (important for caching)
COPY pom.xml .

# Copy source
COPY src ./src

RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]