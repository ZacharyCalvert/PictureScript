package com.zachcalvert.picturescript.export.step;

import com.zachcalvert.picturescript.export.err.StepFailedException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyStep implements OutputStep {

  private static final Logger logger = LoggerFactory.getLogger(CopyStep.class);

  private Path from;

  private Path to;

  public CopyStep(Path from, Path to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute(boolean dryRun) {
    if (to.toFile().exists()) {
      to = resolveConflict(to);
    }
    logger.info("Will copy {} to {}", from.toString(), to.toString());
    try {
      if (!dryRun) {
        FileUtils.copyFile(from.toFile(), to.toFile());
      }
    } catch (Exception e) {
      throw new StepFailedException(String.format("Failure to copy %s to %s", from.toString(), to.toString()), e);
    }
  }
}
