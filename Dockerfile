FROM openjdk:17-jdk-slim
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests
CMD ["java", "-jar", "target/user-service-1.0-SNAPSHOT-jar-with-dependencies.jar"]