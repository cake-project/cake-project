package com.cakemate.cake_platform.domain.payment.customer.exception;

public class PaymentFailedException extends RuntimeException {
  public PaymentFailedException(String message) {
    super(message);
  }
}
