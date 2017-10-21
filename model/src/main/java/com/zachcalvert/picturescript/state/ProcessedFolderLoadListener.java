package com.zachcalvert.picturescript.state;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.state.dto.ProcessedFile;
import java.nio.file.Path;

public interface ProcessedFolderLoadListener {

  void fileMissingFromLoad(FolderBase folderBase, Path path, ProcessedFile processedFile);

  void fileAlreadyExists(FolderBase folderBase, ProcessedFile processedFile, File file);

  void fileLoadedFromState(FolderBase folderBase, ProcessedFile processedFile, File file);
}
