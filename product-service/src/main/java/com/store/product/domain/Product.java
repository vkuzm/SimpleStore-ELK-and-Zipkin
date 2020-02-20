package com.store.product.domain;

import lombok.Data;

@Data
public class Product {
  private long id;
  private String name;
  private String description;
  private int price;
  private int quantity;

  public Product(long id, String name, String description, int price, int quantity) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
  }
}
