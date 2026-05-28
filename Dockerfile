# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY invoicegeneratorapi/pom.xml ./invoicegeneratorapi/
RUN mvn -f invoicegeneratorapi/pom.xml dependency:go-offline -B
COPY invoicegeneratorapi/src ./invoicegeneratorapi/src
RUN mvn -f invoicegeneratorapi/pom.xml clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/invoicegeneratorapi/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
