package com.zachcalvert.picturescript.out.step;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public interface OutputStep {

  void execute(boolean dryRun);

  default Path resolveConflict(Path destination) {
    int iteration = 1;

    String fileName = destination.getFileName().toString();
    String extension = FilenameUtils.getExtension(fileName);
    String name = FilenameUtils.getBaseName(fileName);
    Path parent = destination.getParent();

    while (destination.toFile().exists()) {
      destination = Paths.get(parent.toString(), name + "_" + iteration + "." + extension);
      iteration++;
    }
    return destination;
  }
}
