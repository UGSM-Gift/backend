FROM openjdk:17
LABEL authors="kterr"

ARG JAR_FILE=./build/libs/secretpresent-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
