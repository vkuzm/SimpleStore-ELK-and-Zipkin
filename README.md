<h2>Simple store on microservice architecture</h2>

Logging ELK - Elasticsearch, Logstach, Kibana
Distributed tracing - Spring Sleuth + Zipkin

<h4>How to run?</h2>

1) mvn clean package
2) docker-compose up

<h4>Endpoints</h4>
Gateway - http://localhost:8080/<br>
Registry Service - http://localhost:8761/<br>
Products Service - http://localhost:8080/product-service/api/<br>
Checkout Service - http://localhost:8080/checkout-service/api/<br>
Payment Service - http://localhost:8080/payment-service/api/<br><br>
Zipkin Service - http://localhost:9411/<br>
Kibana UI - http://localhost:5601/<br>


