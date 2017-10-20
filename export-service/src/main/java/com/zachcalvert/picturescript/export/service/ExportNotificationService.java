package com.zachcalvert.picturescript.export.service;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Aggregate notifier of all {@link ExportListener}s
 */
@Service
public class ExportNotificationService {

  private List<ExportListener> exportListeners;

  @Autowired
  public ExportNotificationService(
      Optional<List<ExportListener>> exportListeners) {
    this.exportListeners = exportListeners.orElseGet(ArrayList::new);;
  }

  public void fileMoved(Path from, Path to, File fileUpdated, FolderBase folderBase) {
    exportListeners.stream().forEach((exportListener -> exportListener.fileMoved(from, to, fileUpdated, folderBase)));
  }

  public void fileCopied(Path pathFrom, Path pathTo, File fromFile, File toFile) {
    exportListeners.stream().forEach((exportListener -> exportListener.fileCopied(pathFrom, pathTo, fromFile, toFile)));
  }

  public void fileSkipped(String shaSum) {

    exportListeners.stream().forEach((exportListener -> exportListener.fileSkipped(shaSum)));
  }
}
