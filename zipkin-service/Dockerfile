FROM openjdk:8-jdk-alpine
ADD target/zipkin-service.jar zipkin-service.jar
ENTRYPOINT ["java","-jar","/zipkin-service.jar"]
EXPOSE 9411