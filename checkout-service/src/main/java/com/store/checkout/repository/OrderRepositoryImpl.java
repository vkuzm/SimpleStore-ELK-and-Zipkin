package com.store.checkout.repository;

import com.store.checkout.domain.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final Map<UUID, Order> storage = new HashMap<>();

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public Optional<Order> findById(UUID orderId) {
    return Optional.ofNullable(storage.get(orderId));
  }

  @Override
  public void save(Order order) {
    storage.put(order.getOrderId(), order);
  }
}
