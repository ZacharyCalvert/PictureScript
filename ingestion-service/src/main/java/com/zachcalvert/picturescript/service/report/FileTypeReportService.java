package com.zachcalvert.picturescript.service.report;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.service.ingest.IngestionListener;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileTypeReportService implements IngestionListener {

  private static final Logger logger = LoggerFactory.getLogger(FileTypeReportService.class);

  private HashSet<String> fileTypesProcssed = new HashSet<>();

  private HashSet<String> fileTypesIgnored = new HashSet<>();

  private FileExtensionExtractorService fileExtensionExtractorService;

  @Autowired
  public FileTypeReportService(
      FileExtensionExtractorService fileExtensionExtractorService) {
    this.fileExtensionExtractorService = fileExtensionExtractorService;
  }

  @PreDestroy
  public void logTypesFound() {
    logger.info("File types found and processed: " + sortAndStringifyExtensionSet(fileTypesProcssed));
    logger.info("File types found and ignored: " + sortAndStringifyExtensionSet(fileTypesIgnored));
  }

  private String sortAndStringifyExtensionSet(HashSet<String> set) {
    String[] setArray = set.toArray(new String[set.size()]);
    Arrays.sort(setArray);
    return Arrays.toString(setArray);
  }

  @Override
  public void fileIgnored(FolderBase folderBase, Path path) {
    fileTypesIgnored.add(fileExtensionExtractorService.getExtension(path));
  }

  @Override
  public void fileIngested(FolderBase folderBase, Path path, File file) {
    fileTypesProcssed.add(fileExtensionExtractorService.getExtension(path));
  }
}
