package com.example.inventorysystembackend.exception;

public class ExportFailedException extends RuntimeException {
  public ExportFailedException(String message) {
    super(message);
  }
}
