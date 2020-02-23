package com.store.payment.dto;

import com.store.payment.PaymentResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {

  private String message;
  private PaymentResponseCode code;
}
