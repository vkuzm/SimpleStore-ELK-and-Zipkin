package com.store.checkout.domain;

import com.store.checkout.dto.ProductDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class Order {

  private UUID orderId;
  private String name;
  private String email;
  private String payment;
  private List<ProductDto> products = new ArrayList<>();
}
