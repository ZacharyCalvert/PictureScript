package com.zachcalvert.picturescript.out.step;

import com.zachcalvert.picturescript.err.StepFailedException;
import com.zachcalvert.picturescript.err.StepInvalidException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveStep implements OutputStep {

  private static final Logger logger = LoggerFactory.getLogger(MoveStep.class);

  private Path from;

  private Path to;

  public MoveStep(Path from, Path to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute(boolean dryRun) {
    if (to.equals(from)) {
      logger.info("File move would have no effect.  Skipping for {}", from.toString());
      return;
    }


    if (to.toFile().exists()) {
      to = resolveConflict(to);
    }
    logger.info("Will move {} to {}", from.toString(), to.toString());
    try {
      if (!dryRun) {
        FileUtils.moveFile(from.toFile(), to.toFile());
      }
    } catch (Exception e) {
      throw new StepFailedException(String.format("Failure to mov %s to %s", from.toString(), to.toString()), e);
    }
  }
}
