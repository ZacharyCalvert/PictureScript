package com.zachcalvert.picturescript.service.ingest;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import com.zachcalvert.picturescript.service.util.PathService;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
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

  private FolderBaseRepository folderBaseRepository;

  private IngestionNotificationService ingestionNotificationService;

  private FileProcessorService fileProcessorService;

  private PathService pathService;

  @Autowired
  public DirectoryIngestionService(FileRepository fileRepository,
      FolderBaseRepository folderBaseRepository,
      IngestionNotificationService ingestionNotificationService,
      FileProcessorService fileProcessorService, PathService pathService) {
    this.fileRepository = fileRepository;
    this.folderBaseRepository = folderBaseRepository;
    this.ingestionNotificationService = ingestionNotificationService;
    this.fileProcessorService = fileProcessorService;
    this.pathService = pathService;
  }

  public void processDirectory(boolean input, String directory, List<String> ignoredFileExtensions) {

    logger.debug("Directory configured for processing: " + directory);

    FolderBase folderBase = new FolderBase();
    folderBase.setFromOutput(!input);
    folderBase.setPath(directory);
    folderBaseRepository.save(folderBase);

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
        if (isIgnored(file)) {
          ingestionNotificationService.fileIgnored(folderBase, file);
        } else {
         File processedResult = fileProcessorService.processFile(file, folderBase);
         fileRepository.save(processedResult);
         ingestionNotificationService.fileIngested(folderBase, file, processedResult);
        }
      }
      return FileVisitResult.CONTINUE;
    }
  }
}
