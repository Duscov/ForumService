FROM eclipse-temurin:21-alpine-3.22
WORKDIR /app
COPY target/ForumService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]