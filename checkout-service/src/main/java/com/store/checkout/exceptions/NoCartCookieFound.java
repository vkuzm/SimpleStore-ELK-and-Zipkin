package com.store.checkout.exceptions;

public class NoCartCookieFound extends RuntimeException{

  public NoCartCookieFound(String message) {
    super(message);
  }
}
