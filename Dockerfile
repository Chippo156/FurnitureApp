FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests
# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/DrComputer-0.0.1-SNAPSHOT.war drcomputer.war
EXPOSE 8088

ENTRYPOINT ["java","-jar","drcomputer.war"]

#docker build -t ecommerce:1.0.4 -f ./DockerfileJavaSpring .
#docker login
#docker tag ecommerce:1.0.4 chippo1562003/ecommerce:1.0.4
#docker push chippo1562003/ecommerce:1.0.4