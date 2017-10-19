package com.zachcalvert.picturescript.export.err;

public class StepFailedException extends RuntimeException {

  public StepFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
