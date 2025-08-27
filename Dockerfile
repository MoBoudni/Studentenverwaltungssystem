FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/Studentenverwaltungssystem-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]