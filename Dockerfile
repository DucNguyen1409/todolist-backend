FROM openjdk:17-jdk-slim
COPY target/to-do-backend.jar to-do-backend.jar
ENTRYPOINT ["java", "-jar", "to-do-backend.jar"]