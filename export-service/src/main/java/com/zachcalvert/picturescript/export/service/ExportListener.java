package com.zachcalvert.picturescript.export.service;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.nio.file.Path;

/**
 * Service interface to provide to the Spring IOC for notifications regarding file move, copies,
 * or skipped, but will not be notified on dry runs.
 */
public interface ExportListener {

  void fileMoved(Path from, Path to, File fileUpdated, FolderBase folderBase);

  void fileCopied(Path pathFrom, Path pathTo, File fromFile, File toFile);

  void fileSkipped(String shaSum);
}
