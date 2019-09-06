FROM openjdk:8-jdk-alpine

ARG JAR_FILE

WORKDIR /var/www/html/

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/var/www/html/app.jar"]