package com.zachcalvert.picturescript.repository;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderBaseRepository extends CrudRepository<FolderBase, Long> {

  FolderBase findByPathAndFromOutput(String path, boolean fromOutput);
}
