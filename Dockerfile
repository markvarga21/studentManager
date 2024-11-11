FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/studentmanager-1.0.jar /app/
COPY ./target/classes/schemas/students.xsd /app/schemas/
COPY ./target/classes/schemas/students_draft-07.json /app/schemas/
CMD ["java", "-jar", "studentmanager-1.0.jar"]
