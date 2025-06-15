FROM maven:3.9.9-eclipse-temurin-24 AS build
LABEL author = "ganruilin"
COPY pom.xml /build/pom.xml
COPY src /build/src
WORKDIR /build/
RUN mvn package

FROM eclipse-temurin:24-jre
COPY --from=build /build/target/tweetigel-backend-0.0.1-SNAPSHOT.jar /app/tweetigel-backend-1.jar
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.datasource.url=jdbc:mariadb://db:3306/tweetigel?createDatabaseIfNotExist=true", "-jar", "tweetigel-backend-1.jar"]
