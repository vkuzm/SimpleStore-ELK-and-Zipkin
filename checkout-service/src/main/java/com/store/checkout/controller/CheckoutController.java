package com.store.checkout.controller;

import com.store.checkout.dto.CartDto;
import com.store.checkout.dto.OrderDto;
import com.store.checkout.dto.ProductDto;
import com.store.checkout.exceptions.NoCartCookieFound;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

  private final RestTemplate restTemplate;

  public CheckoutController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
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
  public ResponseEntity<String> makeOrder(@RequestBody OrderDto order) {
    List<ProductDto> products = getProducts(order.getCart());

    order.setProducts(products);
    log.info(order.toString());

    HttpEntity<OrderDto> requestEntity = new HttpEntity<>(order, httpHeaders());
    ResponseEntity<String> response = restTemplate
        .postForEntity("http://localhost:8080/payment-service/api/payment/pay", requestEntity,
            String.class);

    log.info(response.toString());

    if (response.getStatusCode().equals(HttpStatus.OK)) {
      // Save order to DB
    }
    return response;
  }

  private List<ProductDto> getProducts(
      @RequestBody(required = false) List<CartDto> cartCookie) {
    List<ProductDto> products = new ArrayList<>();

    HttpEntity<List<CartDto>> requestEntity = new HttpEntity<>(cartCookie, httpHeaders());
    ResponseEntity<ProductDto[]> response = restTemplate
        .postForEntity("http://localhost:8080/product-service/api/products/findAllByList", requestEntity,
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
}