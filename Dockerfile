# Start with JDK 23 base image
FROM eclipse-temurin:23-jdk AS build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

WORKDIR /app

COPY . .

# Build the app
RUN mvn clean package -DskipTests

# Runtime image
FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]