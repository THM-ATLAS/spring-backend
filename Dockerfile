FROM gradle:7.4.2-jdk17-alpine as base
WORKDIR /home/gradle/project
COPY ./ /home/gradle/project
RUN gradle build

FROM openjdk:17-jdk-alpine  as app
***REMOVED***
WORKDIR /home/gradle/project
EXPOSE 8080
VOLUME /tmp
COPY --from=base /home/gradle/project/build/libs/atlas-backend-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]