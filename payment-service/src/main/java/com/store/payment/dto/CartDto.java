package com.store.payment.dto;

import lombok.Data;

@Data
public class CartDto {

  private long productId;
  private int quantity;
}
