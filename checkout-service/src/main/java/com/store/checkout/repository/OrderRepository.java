package com.store.checkout.repository;

import com.store.checkout.domain.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

  List<Order> findAll();

  Optional<Order> findById(UUID orderId);

  void save(Order order);
}
