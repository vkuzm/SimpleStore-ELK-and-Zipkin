FROM openjdk:8-jdk-alpine
ADD target/product-service.jar product-service.jar
ENTRYPOINT ["java","-jar","/product-service.jar"]
EXPOSE 8080