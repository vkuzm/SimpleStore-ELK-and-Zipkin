package com.store.product.repository;

import com.store.product.domain.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final Map<Long, Product> storage = new HashMap<>();

  {
    storage.put(1L, new Product(1L, "Product name", "Product description", 3500, 10));
    storage.put(2L, new Product(2L, "Product name 2", "Product description 2", 1500, 5));
    storage.put(3L, new Product(3L, "Product name 3", "Product description 3", 2500, 1));
  }

  @Override
  public List<Product> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public Optional<Product> findById(long productId) {
    return Optional.ofNullable(storage.get(productId));
  }

  @Override
  public List<Product> findAllForCart(List<Product> products) {
    return products
        .stream()
        .map(p -> storage.get(p.getProductId()))
        .collect(Collectors.toList());
  }
}
