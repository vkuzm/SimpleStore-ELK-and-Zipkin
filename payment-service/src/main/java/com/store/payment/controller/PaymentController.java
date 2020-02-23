package com.store.payment.controller;

import com.store.payment.PaymentResponseCode;
import com.store.payment.dto.OrderDto;
import com.store.payment.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(path = "/api/payment", produces = "application/json")
public class PaymentController {

  @PostMapping(path = "/pay", consumes = "application/json")
  public ResponseEntity<PaymentResponse> pay(@RequestBody OrderDto order) {

    int sum = order.getProducts()
        .stream()
        .mapToInt(p -> p.getPrice() * p.getQuantity())
        .sum();

    String messageSuccess =
        "Payment \"" + order.getPayment() + "\" with sum: $" + sum + " for customer " + order.getName()
            + " (" + order
            .getEmail() + ") has successfully done. \nYou will be redirected to the home page.";
    String messageFailed = "Failure has occurred while payment process.";

    PaymentResponse paymentResponse;
    if (order.getName() == null || order.getName().isEmpty()
        || order.getPayment() == null || order.getPayment().isEmpty()) {
      paymentResponse = new PaymentResponse(messageFailed, PaymentResponseCode.FAILED);
    } else {
      paymentResponse = new PaymentResponse(messageSuccess, PaymentResponseCode.SUCCESS);
    }

    log.info(order.toString());
    log.info(paymentResponse.toString());

    return ResponseEntity.ok(paymentResponse);
  }
}