package com.zachcalvert.picturescript.out.event;

import com.zachcalvert.picturescript.out.conf.OutputOrder;

public class OrderProcessingCompleteEvent {
  private final OutputOrder outputOrder;

  public OrderProcessingCompleteEvent(OutputOrder outputOrder) {
    this.outputOrder = outputOrder;
  }

  public OutputOrder getOutputOrder() {
    return outputOrder;
  }
}
