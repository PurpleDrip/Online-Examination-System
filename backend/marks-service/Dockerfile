FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/marks-service-1.0.0.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
