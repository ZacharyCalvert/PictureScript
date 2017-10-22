package com.zachcalvert.picturescript.export.service;

import com.zachcalvert.picturescript.export.err.StepFailedException;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileDeliveryService {

  private final Logger logger = LoggerFactory.getLogger(FileDeliveryService.class);

  private ExportNotificationService exportNotificationService;

  private ConflictResolutionStrategyService conflictResolutionStrategyService;

  private FileRepository fileRepository;

  @Autowired
  public FileDeliveryService(
      ExportNotificationService exportNotificationService,
      ConflictResolutionStrategyService conflictResolutionStrategyService,
      FileRepository fileRepository) {
    this.exportNotificationService = exportNotificationService;
    this.conflictResolutionStrategyService = conflictResolutionStrategyService;
    this.fileRepository = fileRepository;
  }

  @Transactional
  public void deliverFile(boolean dryRun, String shaSum, FolderBase outputFolder, String toPath) {
    logger.debug("Request received for processing sha {} to path {}", shaSum, toPath);

    File forMove = fileRepository.findFileFileForMove(shaSum, outputFolder);
    if (forMove == null) {
      executeCopy(dryRun, shaSum, outputFolder, toPath);
    } else {
      if (forMove.getPath().equalsIgnoreCase(toPath)) {
        if (!dryRun) {
          exportNotificationService.fileSkipped(shaSum);
        }
      } else {
        executeMove(dryRun, outputFolder, toPath, forMove);
      }
    }
  }

  private void executeMove(boolean dryRun, FolderBase outputFolder, String toPath, File forMove) {
    Path from = Paths.get(forMove.getPath());
    Path movedResultTo = processFileMove(dryRun, from, toPath);
    forMove.setPath(movedResultTo.toString());
    fileRepository.save(forMove);
    if (!dryRun) {
      exportNotificationService.fileMoved(from, movedResultTo, forMove, outputFolder);
    }
  }

  private void executeCopy(boolean dryRun, String shaSum, FolderBase outputFolder, String toPath) {
    File forCopy = fileRepository.findTopBySha256AndAvailableForExportIsTrue(shaSum);
    Path from = Paths.get(forCopy.getPath());
    Path copiedResultTo = processFileCopy(dryRun, from, toPath);
    File resultingFile = new File(false, forCopy.getSha256(), copiedResultTo.toString(), forCopy.getDateCreated(), forCopy.getEarliestKnownDate(), forCopy.getExtension(), outputFolder, forCopy.getOriginalFileName());
    fileRepository.save(resultingFile);
    if (!dryRun) {
      exportNotificationService.fileCopied(from, copiedResultTo, forCopy, resultingFile);
    }
  }

  protected Path processFileCopy(boolean isDryRun, Path from, String targetToPath) {
    Path to = conflictResolutionStrategyService.ensureNoFilePlacementConflict(Paths.get(targetToPath));
    try {
      if (!isDryRun) {
        FileUtils.copyFile(from.toFile(), to.toFile(), true);
        logger.info("Copy performed from {} to {}", from.toString(), to.toString());
      } else {
        logger.info("Dry run, but would perform copy from {} to {}", from.toString(), to.toString());
      }
      return to;
    } catch (IOException e) {
      logger.error("Error copying file from {} to {}", from.toString(), to.toString(), e);
      throw new StepFailedException("Copy error", e);
    }
  }

  protected Path processFileMove(boolean isDryRun, Path from, String targetToPath) {
    Path to = conflictResolutionStrategyService.ensureNoFilePlacementConflict(Paths.get(targetToPath));
    try {
      if (!isDryRun) {
        FileUtils.moveFile(from.toFile(), to.toFile());
        logger.info("Move performed from {} to {}", from.toString(), to.toString());
      } else {
        logger.info("Dry run, but would perform file move from {} to {}", from.toString(), to.toString());
      }
      return to;
    } catch (IOException e) {
      logger.error("Error moving file from {} to {}", from.toString(), to.toString(), e);
      throw new StepFailedException("File move error", e);
    }
  }
}
