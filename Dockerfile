FROM zlebdg/alpine-openjdk8-jre:latest

COPY target/*.jar /app.jar
