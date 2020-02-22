package com.store.product.contoller;

import com.store.product.domain.Product;
import com.store.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/products", produces = "application/json")
@Slf4j
public class ProductController {

  private final ProductRepository productRepository;

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping
  public ResponseEntity<List<Product>> findAll() {
    List<Product> products = productRepository.findAll();
    log.info(products.toString());
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Optional<Product>> findById(@PathVariable long productId) {
    Optional<Product> product = productRepository.findById(productId);
    log.info(product.toString());
    return ResponseEntity.ok(product);
  }

  @PostMapping("/find")
  public ResponseEntity<List<Product>> findAllById(@RequestBody List<Product> cartProducts) {
    List<Product> products = productRepository.findAllForCart(cartProducts);
    log.info(products.toString());
    return ResponseEntity.ok(products);
  }
}
