# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

COPY target/availability.jar /app.jar

EXPOSE 8084/tcp

CMD ["/usr/bin/java", "-jar", "/app.jar"]