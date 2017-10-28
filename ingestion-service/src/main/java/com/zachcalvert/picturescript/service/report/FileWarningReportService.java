package com.zachcalvert.picturescript.service.report;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.service.ingest.IngestionListener;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileWarningReportService implements ReportService, IngestionListener {

  private static final Logger logger = LoggerFactory.getLogger(FileWarningReportService.class);

  private static final String[] DEFAULT_WARN_ON = new String[] {"zip", "bup", "ifo", "vob", "iso"};

  private Set<String> warnOn;

  private ArrayList<String> foundToWarn;

  private FileExtensionExtractorService fileExtensionExtractorService;

  @Autowired
  public FileWarningReportService(
      FileExtensionExtractorService fileExtensionExtractorService) {
    this(Arrays.asList(DEFAULT_WARN_ON), fileExtensionExtractorService);
  }

  public FileWarningReportService(List<String> suspectExtensions,
      FileExtensionExtractorService fileExtensionExtractorService) {
    warnOn = new HashSet<>(suspectExtensions);
    this.fileExtensionExtractorService = fileExtensionExtractorService;
    this.foundToWarn = new ArrayList<>();
  }

  @Override
  public void fileIgnored(FolderBase folderBase, Path path) {
    pathReceivedForInspection(path);
  }

  @Override
  public void fileIngested(FolderBase folderBase, Path path, File file) {
    pathReceivedForInspection(path);
  }

  private void pathReceivedForInspection(Path path) {
    String ext = StringUtils.lowerCase(fileExtensionExtractorService.getExtension(path));
    if (warnOn.contains(ext)) {
      String issuePath = path.toAbsolutePath().toString();
      logger.warn("File found which will be problematic for processing: " + issuePath);
      foundToWarn.add(issuePath);
    }
  }

  @Override
  public void logReport() {
    if (foundToWarn.size() > 0) {
      logger.warn("{} problematic files found.", foundToWarn.size());
      for (String issueFile:foundToWarn) {
        logger.warn("File will need consideration for different processing: {}", issueFile);
      }
    }
  }
}
