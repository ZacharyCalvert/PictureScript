package com.zachcalvert.picturescript.service.ingest;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.nio.file.Path;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngestionNotificationService {

  private List<IngestionListener> ingestionListeners;

  @Autowired
  public IngestionNotificationService(
      List<IngestionListener> ingestionListeners) {
    this.ingestionListeners = ingestionListeners;
  }

  public void fileIgnored(FolderBase folderBase, Path path) {
    for (IngestionListener ingestionListener:ingestionListeners) {
      ingestionListener.fileIgnored(folderBase, path);
    }
  }

  public void fileIngested(FolderBase folderBase, Path path, File file) {
    for (IngestionListener ingestionListener:ingestionListeners) {
      ingestionListener.fileIngested(folderBase, path, file);
    }
  }
}
