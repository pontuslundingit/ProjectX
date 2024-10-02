FROM gradle:jdk21 AS builder

COPY ./ ./

RUN gradle build

RUN mv ./build/libs/ProjectX-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]