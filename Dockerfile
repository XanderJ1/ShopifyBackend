FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk

WORKDIR /app

COPY /target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]