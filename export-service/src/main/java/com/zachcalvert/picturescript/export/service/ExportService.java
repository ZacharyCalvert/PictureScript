package com.zachcalvert.picturescript.export.service;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.substitution.PathSubsitutionService;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.service.FolderBaseService;
import com.zachcalvert.picturescript.state.ProcessedFileStateManagementService;
import com.zachcalvert.picturescript.state.dto.ProcessedFolderState;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

  private static final Logger logger = LoggerFactory.getLogger(ExportService.class);

  private DryRunService dryRunService;

  private FolderBaseService folderBaseService;

  private FileDeliveryService fileDeliveryService;

  private PathSubsitutionService pathSubsitutionService;

  private FileRepository fileRepository;

  private ProcessedFileStateManagementService processedFileStateManagementService;

  public ExportService(@Autowired(required = false) DryRunService dryRunService,
      @Autowired FolderBaseService folderBaseService,
      @Autowired FileDeliveryService fileDeliveryService,
      @Autowired PathSubsitutionService pathSubsitutionService,
      @Autowired FileRepository fileRepository,
      @Autowired ProcessedFileStateManagementService processedFileStateManagementService) {
    this.dryRunService = dryRunService;
    this.folderBaseService = folderBaseService;
    this.fileDeliveryService = fileDeliveryService;
    this.pathSubsitutionService = pathSubsitutionService;
    this.fileRepository = fileRepository;
    this.processedFileStateManagementService = processedFileStateManagementService;
  }

  public void processExport(ExportRequest exportRequest) {
    List<String> allShaSums = exportRequest.getShaSumIds();
    FolderBase outputFolderBase = folderBaseService.createOrFindFolderBase(exportRequest.getBaseOutputPath().toString());
    processedFileStateManagementService.attemptLoadPreviouslyProcessedDirectory(outputFolderBase, false);
    boolean isDryRun = dryRunService == null ? false : dryRunService.isDryRun();
    for (String shaSum:allShaSums) {
      File fileForExport = fileRepository.findTopBySha256AndAvailableForExportIsTrue(shaSum);
      Path to = pathSubsitutionService.constructTargetOutputPath(shaSum, Paths.get(fileForExport.getPath()), exportRequest);
      fileDeliveryService.deliverFile(isDryRun, shaSum, outputFolderBase, to.toString());
    }
    try {
      processedFileStateManagementService.writeStateToFile(Paths.get(outputFolderBase.getPath(),
          ProcessedFolderState.DEFAULT_FILE_NAME), processedFileStateManagementService.extractState(outputFolderBase));
    } catch (Exception e) {
      logger.error("Error creating state definition", e);
      throw new RuntimeException("Could not process folder state output", e);
    }
  }
}
