FROM openjdk:8-jdk-alpine
ADD target/payment-service.jar payment-service.jar
ENTRYPOINT ["java","-jar","/payment-service.jar"]
EXPOSE 8080