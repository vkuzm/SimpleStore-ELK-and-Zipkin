package com.store.checkout.dto;

import lombok.Data;

@Data
public class ProductDto {
  private long productId;
  private String name;
  private String description;
  private int price;
  private int quantity;
}
