FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8088
CMD ["java", "-jar", "app.jar"]


#docker build -t ecommerce:1.0.4 -f ./DockerfileJavaSpring .
#docker login
#docker tag ecommerce:1.0.4 chippo1562003/ecommerce:1.0.4
#docker push chippo1562003/ecommerce:1.0.4