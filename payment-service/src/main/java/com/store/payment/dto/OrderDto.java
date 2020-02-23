package com.store.payment.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

  private String name;
  private String email;
  private String payment;
  private List<CartDto> cart = new ArrayList<>();
  private List<ProductDto> products = new ArrayList<>();
}
