FROM openjdk
ADD target/zipkin-service.jar zipkin-service.jar
CMD java $JAVA_OPTIONS -jar /zipkin-service.jar
EXPOSE 9411