#Build
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/szteam-network-*.jar /app/

EXPOSE 8088

ENV DB_URL=jdbc:postgresql://postgres-sql-szt:5432/szteam_network
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} szteam-network-api-${JAR_VERSION}.jar