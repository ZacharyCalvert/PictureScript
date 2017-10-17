package com.zachcalvert.picturescript.service.ingest;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.nio.file.Path;

public interface IngestionListener {

  void fileIgnored(FolderBase folderBase, Path path);

  void fileIngested(FolderBase folderBase, Path path, File file);
}
