FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle ../.. /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/cafe-management-1.jar
ENTRYPOINT ["java", "-jar", "/app/cafe-management-1.jar"]