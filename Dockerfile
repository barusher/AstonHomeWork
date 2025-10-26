FROM openjdk:17-jdk-slim
LABEL author="brnvvv"
WORKDIR /app
COPY target/AstonHomeWork-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]