package com.zachcalvert.picturescript.out.event;

import com.zachcalvert.picturescript.export.step.CopyStep;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import java.nio.file.Path;

public class FileCopiedEvent extends FileDeliveredEvent {

  public FileCopiedEvent(OutputOrder order,
      CopyStep outputStep,
      File file, Path from, Path to) {
    super(order, outputStep, file, from, to);
  }
}
