FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src/
RUN mvn clean package -DskipTests


# Run stage

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY src/main/resources/templates/ ./templates/
COPY src/main/resources/static/ ./static/
COPY --from=build /app/target/EletricShop-0.0.1-SNAPSHOT.war EletricShop.war
EXPOSE 8080

ENTRYPOINT ["java","-jar","EletricShop.war"]