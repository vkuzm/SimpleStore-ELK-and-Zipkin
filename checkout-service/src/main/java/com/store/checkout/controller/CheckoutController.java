package com.store.checkout.controller;

import com.store.checkout.domain.Order;
import com.store.checkout.dto.CartDto;
import com.store.checkout.dto.OrderDto;
import com.store.checkout.dto.ProductDto;
import com.store.checkout.exceptions.NoCartCookieFound;
import com.store.checkout.repository.OrderRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(path = "/api/checkout", produces = "application/json")
public class CheckoutController {

  private final OrderRepository orderRepository;
  private final RestTemplate restTemplate;

  public CheckoutController(OrderRepository orderRepository,
      RestTemplate restTemplate) {
    this.orderRepository = orderRepository;
    this.restTemplate = restTemplate;
  }

  @GetMapping("/orders")
  public ResponseEntity<List<Order>> findAll() {
    return ResponseEntity.ok(orderRepository.findAll());
  }

  @GetMapping("/orders/{orderId}")
  public ResponseEntity<Order> findById(@PathVariable UUID orderId) {
    Optional<Order> order = orderRepository.findById(orderId);
    return order.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.ok().build());
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<List<ProductDto>> getProductInCart(
      @RequestBody(required = false) List<CartDto> cartCookie) {
    if (cartCookie == null || cartCookie.isEmpty()) {
      throw new NoCartCookieFound("No cookies of the cart have found");
    }
    List<ProductDto> products = getProducts(cartCookie);
    return ResponseEntity.ok(products);
  }

  @PostMapping("/makeOrder")
  public ResponseEntity<String> makeOrder(@RequestBody OrderDto orderDto) {
    List<ProductDto> products = getProducts(orderDto.getCart());

    orderDto.setProducts(products);
    log.info(orderDto.toString());

    HttpEntity<OrderDto> requestEntity = new HttpEntity<>(orderDto, httpHeaders());
    ResponseEntity<String> response = restTemplate
        .postForEntity("http://gateway-service:8080/payment-service/api/payment/pay", requestEntity,
            String.class);

    log.info(response.toString());

    if (response.getStatusCode().equals(HttpStatus.OK)) {
      Order order = convertToEntity(orderDto);
      orderRepository.save(order);
    }
    return response;
  }

  private List<ProductDto> getProducts(
      @RequestBody(required = false) List<CartDto> cartCookie) {
    List<ProductDto> products = new ArrayList<>();

    HttpEntity<List<CartDto>> requestEntity = new HttpEntity<>(cartCookie, httpHeaders());
    ResponseEntity<ProductDto[]> response = restTemplate
        .postForEntity("http://gateway-service:8080/product-service/api/products/findAllByList",
            requestEntity,
            ProductDto[].class);

    if (response.getBody() != null) {
      products = Arrays.asList(response.getBody());
    }
    log.info(cartCookie.toString());
    log.info(products.toString());
    return products;
  }

  public static HttpHeaders httpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  private Order convertToEntity(OrderDto orderDto) {
    Order order = new Order();
    order.setOrderId(UUID.randomUUID());
    order.setName(orderDto.getName());
    order.setEmail(orderDto.getEmail());
    order.setPayment(orderDto.getPayment());
    order.setProducts(orderDto.getProducts());
    return order;
  }
}