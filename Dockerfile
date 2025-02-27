FROM maven:3.8.3-amazoncorretto-17 AS builder
WORKDIR /app
COPY . .
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/TaskManager-*.jar /app/app.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/app.jar"]