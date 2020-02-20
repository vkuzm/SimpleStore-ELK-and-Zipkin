package com.store.product.contoller;

import com.store.product.domain.Product;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/products", produces = "application/json")
public class ProductController {

  @GetMapping
  public ResponseEntity<List<Product>> findAll() {
    List<Product> products = Arrays.asList(
        new Product(1L, "Product name", "Product description", 3500, 10),
        new Product(2L, "Product name 2", "Product description 2", 1500, 5),
        new Product(3L, "Product name 3", "Product description 3", 2500, 1)
    );
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Product> findById(@PathVariable long productId) {
    Product product = new Product(productId, "Product name", "Product description", 3500, 10);
    return ResponseEntity.ok(product);
  }
}
