package com.store.checkout.controller;

import com.store.checkout.dto.CartDto;
import com.store.checkout.dto.OrderDto;
import com.store.checkout.dto.ProductDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

  @PostMapping(consumes = "application/json")
  public ResponseEntity<List<ProductDto>> getProductInCart(
      @RequestBody(required = false) List<CartDto> cartCookie) {
    if (cartCookie == null || cartCookie.isEmpty()) {
      throw new RuntimeException("No cookies of the cart have found");
    }
    log.info(cartCookie.toString());

    List<ProductDto> products = new ArrayList<>();

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<List<CartDto>> requestEntity = new HttpEntity<>(cartCookie, httpHeaders());

    ResponseEntity<ProductDto[]> response = restTemplate
        .postForEntity("http://localhost:8081/api/products/find", requestEntity,
            ProductDto[].class);

    if (response.getBody() != null) {
      products = Arrays.asList(response.getBody());
    }
    log.info(products.toString());
    return ResponseEntity.ok(products);
  }

  @PostMapping("/makeOrder")
  public ResponseEntity makeOrder(@RequestBody OrderDto order) {
    log.info(order.toString());
    return ResponseEntity.ok().build();
  }

  public static HttpHeaders httpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}