FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

RUN mv target/desafio-todolist-*.jar target/todolist-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/todolist-app.jar"]