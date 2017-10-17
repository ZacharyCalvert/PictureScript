package com.zachcalvert.picturescript.out.event;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.step.MoveStep;
import com.zachcalvert.picturescript.out.step.OutputStep;
import java.nio.file.Path;

public class FileMovedEvent extends FileDeliveredEvent {

  public FileMovedEvent(OutputOrder order,
      MoveStep outputStep,
      File file, Path from, Path to) {
    super(order, outputStep, file, from, to);
  }
}