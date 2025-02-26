# Use an official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the target folder
COPY ./target/shopify-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
