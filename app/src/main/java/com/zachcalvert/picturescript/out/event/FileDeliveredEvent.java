package com.zachcalvert.picturescript.out.event;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.step.OutputStep;
import java.nio.file.Path;

public abstract class FileDeliveredEvent extends StepProcessedEvent {

  private final File file;

  private final Path from;

  private final Path to;

  public FileDeliveredEvent(OutputOrder order,
      OutputStep outputStep, File file, Path from, Path to) {
    super(order, outputStep);
    this.file = file;
    this.from = from;
    this.to = to;
  }

  public File getFile() {
    return file;
  }

  public Path getFrom() {
    return from;
  }

  public Path getTo() {
    return to;
  }
}
