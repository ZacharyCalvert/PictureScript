package com.zachcalvert.picturescript.out.step;

import com.zachcalvert.picturescript.err.StepFailedException;
import com.zachcalvert.picturescript.err.StepInvalidException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

public class MoveStep implements OutputStep {

  private Path from;

  private Path to;

  public MoveStep(Path from, Path to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute() {
    if (to.toFile().exists()) {
      throw new StepInvalidException(String.format("Cannot move %s to %s", from.toString(), to.toString()));
    }
    try {
      FileUtils.moveFile(from.toFile(), to.toFile());
    } catch (Exception e) {
      throw new StepFailedException(String.format("Failure to mov %s to %s", from.toString(), to.toString()), e);
    }
  }
}
