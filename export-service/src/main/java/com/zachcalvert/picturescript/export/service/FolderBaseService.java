package com.zachcalvert.picturescript.export.service;

import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FolderBaseService {

  private FolderBaseRepository folderBaseRepository;

  @Autowired
  public FolderBaseService(
      FolderBaseRepository folderBaseRepository) {
    this.folderBaseRepository = folderBaseRepository;
  }

  @Transactional
  public FolderBase createOrFindFolderBase(String path) {
    FolderBase folderBase = folderBaseRepository.findByPath(path);
    if (folderBase == null) {
      folderBase = new FolderBase();
      folderBase.setPath(path);
      folderBaseRepository.save(folderBase);
    }
    return folderBase;
  }

}
