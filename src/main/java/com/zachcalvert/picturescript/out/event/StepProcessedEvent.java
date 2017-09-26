package com.zachcalvert.picturescript.out.event;

import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.step.OutputStep;

public abstract class StepProcessedEvent {

  private final OutputOrder order;

  private final OutputStep outputStep;

  public StepProcessedEvent(OutputOrder order,
      OutputStep outputStep) {
    this.order = order;
    this.outputStep = outputStep;
  }

  public OutputOrder getOrder() {
    return order;
  }

  public OutputStep getOutputStep() {
    return outputStep;
  }
}
