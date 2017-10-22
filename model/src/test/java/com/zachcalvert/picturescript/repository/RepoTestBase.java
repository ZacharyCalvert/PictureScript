package com.zachcalvert.picturescript.repository;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.time.Instant;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RepoTestBase {

  @Autowired
  protected FileRepository fileRepository;

  @Autowired
  protected FolderBaseRepository folderBaseRepository;

  protected FolderBase in1;

  protected FolderBase in2;

  protected FolderBase out1;

  protected FolderBase out2;

  @Before
  public void prepareFoundation() {
    fileRepository.deleteAll();
    folderBaseRepository.deleteAll();
    in1 = constructAndSaveBase("in1", false);
    in2 = constructAndSaveBase("in2", false);
    out1 = constructAndSaveBase("out1", true);
    out2 = constructAndSaveBase("out2", true);
  }

  protected FolderBase constructAndSaveBase(String from, boolean fromOutput) {
    FolderBase result = new FolderBase(from);
    folderBaseRepository.save(result);
    return result;
  }

  protected File constructAndSaveFile(String sha256, boolean forExport, String path, Instant dateCreated, Instant earliestKnownDate,
      String extension, FolderBase folderBase) {
    File file = new File(forExport, sha256, path, dateCreated, earliestKnownDate, extension, folderBase, path);
    fileRepository.save(file);
    return file;
  }
}
