FROM amazoncorretto:17 as BUILD

WORKDIR /app

COPY src /app/src
COPY pom.xml /app
COPY .mvn /app/.mvn
COPY mvnw /app/mvnw

RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:17 as RUNTIME

COPY --from=BUILD /app/target/univan-gateway-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]