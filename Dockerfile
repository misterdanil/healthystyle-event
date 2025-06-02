FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /event

COPY ../util/pom.xml /util/pom.xml
WORKDIR /util
RUN mvn dependency:go-offline -B

COPY ../util .
RUN mvn -f /util/pom.xml clean install -DskipTests

WORKDIR /event

COPY ./event/pom.xml .
COPY ./event/event-model/pom.xml ./event-model/pom.xml
COPY ./event/event-repository/pom.xml ./event-repository/pom.xml
COPY ./event/event-service/pom.xml ./event-service/pom.xml
COPY ./event/event-web/pom.xml ./event-web/pom.xml
COPY ./event/event-app/pom.xml ./event-app/pom.xml

RUN mvn dependency:go-offline -B

COPY ./event .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /event
COPY --from=build /event/event-app/target/*.jar event.jar
EXPOSE 3001
ENTRYPOINT ["java", "-jar", "event.jar"]
