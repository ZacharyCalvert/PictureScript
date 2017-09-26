package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.conf.YmlOutputTemplate;
import com.zachcalvert.picturescript.out.event.FileCopiedEvent;
import com.zachcalvert.picturescript.out.event.FileMovedEvent;
import com.zachcalvert.picturescript.out.event.OrderProcessingCompleteEvent;
import com.zachcalvert.picturescript.out.step.CopyStep;
import com.zachcalvert.picturescript.out.step.MoveStep;
import com.zachcalvert.picturescript.out.step.OutputStep;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDeliveryService {

  private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryService.class);

  private final FileRepository fileRepository;

  private final FolderBaseRepository folderBaseRepository;

  private final PathSubsitutionService pathSubsitutionService;

  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public OrderDeliveryService(FileRepository fileRepository,
      FolderBaseRepository folderBaseRepository,
      PathSubsitutionService pathSubsitutionService,
      ApplicationEventPublisher applicationEventPublisher) {
    this.fileRepository = fileRepository;
    this.folderBaseRepository = folderBaseRepository;
    this.pathSubsitutionService = pathSubsitutionService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void processOrder(OutputOrder order) {
    YmlOutputTemplate ymlOutputTemplate = order.getYmlOutputTemplate();
    FolderBase outputFolderBase = folderBaseRepository.findByPathAndFromOutput(order.getBaseOutputPath().toString(), true);
    List<String> allShaSums = fileRepository.findRequiredOutputShaSums(ymlOutputTemplate.getTypes());

    List<String> moves = fileRepository.findMoveShaSums(allShaSums, outputFolderBase);
    List<String> copies = allShaSums.stream().filter(sha -> !moves.contains(sha)).collect(Collectors.toList());
    logger.info("All sha sums {}", Arrays.toString(allShaSums.toArray(new String[]{})));
    logger.info("Move sha sums {}", Arrays.toString(moves.toArray(new String[]{})));
    logger.info("Copy sha sums {}", Arrays.toString(copies.toArray(new String[]{})));

    for (String copy:copies) {
      processCopy(copy, order);
    }
    for (String move:moves) {
      processMove(move, outputFolderBase, order);
    }
    applicationEventPublisher.publishEvent(new OrderProcessingCompleteEvent(order));
  }

  @Transactional(readOnly = true)
  protected void processCopy(String shaShum, OutputOrder order) {
    File copyFile = fileRepository.findTopBySha256OrderByEarliestKnownDateDesc(shaShum);
    Path from = Paths.get(copyFile.getPath());
    Path to = pathSubsitutionService.constructTargetOutputPath(shaShum, from, order);
    CopyStep copy = new CopyStep(from, to);
    copy.execute(order.isDryRun());
    FileCopiedEvent event = new FileCopiedEvent(order, copy, copyFile, from, to);
    applicationEventPublisher.publishEvent(event);
  }

  @Transactional
  protected void processMove(String shaShum, FolderBase folderBase, OutputOrder order) {
    File moveFile = fileRepository.findFileFileForMove(shaShum, folderBase);
    Path from = Paths.get(moveFile.getPath());
    Path to = pathSubsitutionService.constructTargetOutputPath(shaShum, from, order);
    MoveStep move = new MoveStep(from, to);
    move.execute(order.isDryRun());
    FileMovedEvent event = new FileMovedEvent(order, move, moveFile, from, to);
    applicationEventPublisher.publishEvent(event);
  }
}
