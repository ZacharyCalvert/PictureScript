package com.zachcalvert.picturescript.service.ingest;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import com.zachcalvert.picturescript.repository.service.FolderBaseService;
import com.zachcalvert.picturescript.service.util.PathService;
import com.zachcalvert.picturescript.state.ProcessedFileStateManagementService;
import com.zachcalvert.picturescript.state.ProcessedFolderLoadListener;
import com.zachcalvert.picturescript.state.dto.ProcessedFile;
import com.zachcalvert.picturescript.state.dto.ProcessedFolderState;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectoryIngestionService {

  private static final Logger logger = LoggerFactory.getLogger(DirectoryIngestionService.class);

  private FileRepository fileRepository;

  private FolderBaseService folderBaseService;

  private IngestionNotificationService ingestionNotificationService;

  private FileProcessorService fileProcessorService;

  private PathService pathService;

  private ProcessedFileStateManagementService processedFileStateManagementService;

  @Autowired
  public DirectoryIngestionService(
      FileRepository fileRepository,
      FolderBaseService folderBaseService,
      IngestionNotificationService ingestionNotificationService,
      FileProcessorService fileProcessorService,
      PathService pathService,
      ProcessedFileStateManagementService processedFileStateManagementService) {
    this.fileRepository = fileRepository;
    this.folderBaseService = folderBaseService;
    this.ingestionNotificationService = ingestionNotificationService;
    this.fileProcessorService = fileProcessorService;
    this.pathService = pathService;
    this.processedFileStateManagementService = processedFileStateManagementService;
  }

  public void processDirectory(boolean input, String directory, List<String> ignoredFileExtensions) {

    logger.debug("Directory configured for processing: " + directory);

    FolderBase folderBase = folderBaseService.createOrFindFolderBase(directory);
    processedFileStateManagementService.attemptLoadPreviouslyProcessedDirectory(folderBase, true);

    try {
      Path dirPath = pathService.getPath(directory);
      logger.debug("Full directory path for input processing: " + dirPath.toString());
      Files.walkFileTree(dirPath, new FileFinder(folderBase, ignoredFileExtensions));
    } catch (Exception e) {
      logger.error("Error loading directory: " + directory, e);
    }
  }

  private class FileFinder extends SimpleFileVisitor<Path> {

    private FolderBase folderBase;

    private List<String> ignoredFileExtensions;

    public FileFinder(FolderBase folderBase, List<String> ignoredFileExtensions) {
      this.folderBase = folderBase;
      this.ignoredFileExtensions = ignoredFileExtensions;
    }

    private boolean isIgnored(Path file) {
      String type = StringUtils.lowerCase(FilenameUtils.getExtension(file.toString()));
      return ignoredFileExtensions.contains(type);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      if (attrs.isRegularFile() && ! isIgnored(file)) {
        if (isIgnored(file) || StringUtils.startsWith(FilenameUtils.getName(file.toString()), ".")) {
          ingestionNotificationService.fileIgnored(folderBase, file);
        } else {

         File existingResult = fileRepository.findFileByPath(file.toString());
         if (existingResult != null) {
           existingResult.setAvailableForExport(true);
           fileRepository.save(existingResult);
         } else {
           File processedResult = fileProcessorService.processFile(file, folderBase);
           processedResult.setAvailableForExport(true);
           processedResult.setOriginalFileName(file.toAbsolutePath().toString());
           fileRepository.save(processedResult);
           ingestionNotificationService.fileIngested(folderBase, file, processedResult);
         }
        }
      }
      return FileVisitResult.CONTINUE;
    }
  }
}
