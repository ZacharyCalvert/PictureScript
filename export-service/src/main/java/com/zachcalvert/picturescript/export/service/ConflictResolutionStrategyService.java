package com.zachcalvert.picturescript.export.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

@Service
public class ConflictResolutionStrategyService {

  public Path ensureNoFilePlacementConflict(Path destination) {
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
