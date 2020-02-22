package com.store.product.repository;

import com.store.product.domain.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  List<Product> findAll();

  Optional<Product> findById(long productId);

  List<Product> findAllForCart(List<Product> products);
}
