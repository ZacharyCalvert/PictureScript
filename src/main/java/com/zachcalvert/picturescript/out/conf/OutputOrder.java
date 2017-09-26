package com.zachcalvert.picturescript.out.conf;

import java.nio.file.Path;

public class OutputOrder {

  private Path baseOutputPath;

  private YmlOutputTemplate ymlOutputTemplate;

  public OutputOrder(Path basePath,
      YmlOutputTemplate ymlOutputTemplate) {
    this.baseOutputPath = basePath;
    this.ymlOutputTemplate = ymlOutputTemplate;
  }

  public Path getBaseOutputPath() {
    return baseOutputPath;
  }

  public YmlOutputTemplate getYmlOutputTemplate() {
    return ymlOutputTemplate;
  }
}
