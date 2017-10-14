package com.zachcalvert.picturescript.repository;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Long> {

  @Query("SELECT DISTINCT f.sha256 FROM File f WHERE f.extension in :fileTypes and f.folderBase.fromOutput = false")
  List<String> findRequiredOutputShaSums(@Param("fileTypes") List<String> fileTypes);

  @Query("SELECT DISTINCT f.sha256 FROM File f WHERE f.sha256 in :shaSums and f.folderBase = :folderBase")
  List<String> findMoveShaSums(@Param("shaSums") List<String> shaSums, @Param("folderBase") FolderBase folderBase);

  @Query("SELECT f FROM File f WHERE f.sha256 = :sha and f.folderBase = :folderBase")
  File findFileFileForMove(@Param("sha") String sha, @Param("folderBase") FolderBase folderBase);

  File findTopBySha256OrderByEarliestKnownDateDesc(String shaSum);
}
