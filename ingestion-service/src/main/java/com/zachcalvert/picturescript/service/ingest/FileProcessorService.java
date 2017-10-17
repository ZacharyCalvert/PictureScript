package com.zachcalvert.picturescript.service.ingest;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.service.meta.DrewExifExtractor;
import com.zachcalvert.picturescript.service.meta.FileMetadata;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import com.zachcalvert.picturescript.service.util.ShaSumService;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorService {

  private static final Logger logger = LoggerFactory.getLogger(FileProcessorService.class);

  private ShaSumService shaSumService;

  private DrewExifExtractor drewExifExtractor;

  private FileExtensionExtractorService fileExtensionExtractorService;

  @Autowired
  public FileProcessorService(ShaSumService shaSumService,
      DrewExifExtractor drewExifExtractor,
      FileExtensionExtractorService fileExtensionExtractorService) {
    this.shaSumService = shaSumService;
    this.drewExifExtractor = drewExifExtractor;
    this.fileExtensionExtractorService = fileExtensionExtractorService;
  }

  public File processFile(Path filePath, FolderBase folderBase) {

    File file = new File();
    String sha256 = null;
    try {
      sha256 = shaSumService.sha256(filePath.toFile());
      logger.info("SHA256: {} : {}", filePath.toString(), sha256);
    } catch (Exception e) {
      logger.error("Error processing sha256", e);
      throw new RuntimeException("Failed to calculate sha checksum", e);
    }
    file.setPath(filePath.toAbsolutePath().toString());
    file.setSha256(sha256);

    FileMetadata fileMetadata = drewExifExtractor.extractMetaData(filePath.toFile());
    file.setDateCreated(fileMetadata.getFileDateCreated());
    file.setEarliestKnownDate(fileMetadata.getEarliestMetaDate());
    file.setFolderBase(folderBase);
    file.setExtension(fileExtensionExtractorService.getExtension(filePath));

    return file;
  }

}
