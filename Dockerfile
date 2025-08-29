FROM openjdk:17-jdk-alpine

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} appRestService.jar

ENTRYPOINT ["java","-jar","/appRestService.jar"]