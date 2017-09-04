package com.zachcalvert.picturescript.repository;

import com.zachcalvert.picturescript.model.File;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Long> {

  @Query("SELECT DISTINCT f.sha256 FROM File f WHERE f.extension in :fileTypes and f.folderBase.fromOutput = false")
  List<String> findRequiredOutputShaSums(@Param("fileTypes") List<String> fileTypes);
}
