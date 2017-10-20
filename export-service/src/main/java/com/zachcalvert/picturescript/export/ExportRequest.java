package com.zachcalvert.picturescript.export;

import java.nio.file.Path;
import java.util.List;

public class ExportRequest {

  private Path baseOutputPath;

  private List<String> shaSumIds;

  private List<String> pathFormats;

  public ExportRequest() {
  }

  public ExportRequest(Path baseOutputPath, List<String> shaSumIds,
      List<String> pathFormats) {
    this.baseOutputPath = baseOutputPath;
    this.shaSumIds = shaSumIds;
    this.pathFormats = pathFormats;
  }

  public Path getBaseOutputPath() {
    return baseOutputPath;
  }

  public void setBaseOutputPath(Path baseOutputPath) {
    this.baseOutputPath = baseOutputPath;
  }

  public List<String> getShaSumIds() {
    return shaSumIds;
  }

  public void setShaSumIds(List<String> shaSumIds) {
    this.shaSumIds = shaSumIds;
  }

  public List<String> getPathFormats() {
    return pathFormats;
  }

  public void setPathFormats(List<String> pathFormats) {
    this.pathFormats = pathFormats;
  }
}
