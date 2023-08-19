FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/usermanager-1.0.jar /app/
CMD ["java", "-jar", "usermanager-1.0.jar"]
