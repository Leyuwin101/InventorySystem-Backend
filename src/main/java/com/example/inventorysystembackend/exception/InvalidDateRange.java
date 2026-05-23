package com.example.inventorysystembackend.exception;

public class InvalidDateRange extends RuntimeException {
  public InvalidDateRange(String message) {
    super(message);
  }
}
