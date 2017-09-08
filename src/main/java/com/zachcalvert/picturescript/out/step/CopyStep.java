package com.zachcalvert.picturescript.out.step;

import com.zachcalvert.picturescript.err.StepFailedException;
import com.zachcalvert.picturescript.err.StepInvalidException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

public class CopyStep implements OutputStep {

  private Path from;

  private Path to;

  public CopyStep(Path from, Path to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute() {
    if (to.toFile().exists()) {
      throw new StepInvalidException(String.format("Cannot copy %s to %s", from.toString(), to.toString()));
    }
    try {
      FileUtils.copyFile(from.toFile(), to.toFile());
    } catch (Exception e) {
      throw new StepFailedException(String.format("Failure to copy %s to %s", from.toString(), to.toString()), e);
    }
  }
}
