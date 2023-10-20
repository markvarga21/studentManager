FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/studentmanager-1.0.jar /app/
CMD ["java", "-jar", "studentmanager-1.0.jar"]
