# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

COPY target/users.jar /app.jar

CMD ["/usr/bin/java", "-jar", "/app.jar"]