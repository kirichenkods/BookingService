FROM maven:3.8.5-openjdk-17 AS builder
COPY . /booking
WORKDIR /booking
RUN mvn -f pom.xml clean package -D maven.test.skip=true

FROM openjdk:17-slim
COPY --from=builder /booking/target/*.jar /booking.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/booking.jar"]