package com.zachcalvert.picturescript.out.conf;

import java.nio.file.Path;

public class OutputOrder {

  private Path baseOutputPath;

  private YmlOutputTemplate ymlOutputTemplate;

  private boolean dryRun;

  public OutputOrder(Path baseOutputPath,
      YmlOutputTemplate ymlOutputTemplate, boolean dryRun) {
    this.baseOutputPath = baseOutputPath;
    this.ymlOutputTemplate = ymlOutputTemplate;
    this.dryRun = dryRun;
  }

  public Path getBaseOutputPath() {
    return baseOutputPath;
  }

  public YmlOutputTemplate getYmlOutputTemplate() {
    return ymlOutputTemplate;
  }

  public boolean isDryRun() {
    return dryRun;
  }
}
