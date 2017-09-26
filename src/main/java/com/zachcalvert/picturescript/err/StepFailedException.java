package com.zachcalvert.picturescript.err;

public class StepFailedException extends RuntimeException {

  public StepFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
