FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY src src
COPY pom.xml pom.xml

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

# Копируем JAR из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт и запускаем приложение
EXPOSE 8082
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
