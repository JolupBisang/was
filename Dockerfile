FROM amazoncorretto:17 AS builder
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM amazoncorretto:17
WORKDIR /opt/app
COPY --from=builder build/libs/*.jar /opt/app/spring-boot-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-application.jar"]
