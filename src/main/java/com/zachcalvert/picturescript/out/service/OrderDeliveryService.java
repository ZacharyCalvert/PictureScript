package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.conf.YmlOutputTemplate;
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
import org.springframework.stereotype.Service;

@Service
public class OrderDeliveryService {

  private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryService.class);

  private final FileRepository fileRepository;

  private final FolderBaseRepository folderBaseRepository;

  private final PathSubsitutionService pathSubsitutionService;


  @Autowired
  public OrderDeliveryService(FileRepository fileRepository,
      FolderBaseRepository folderBaseRepository,
      PathSubsitutionService pathSubsitutionService) {
    this.fileRepository = fileRepository;
    this.folderBaseRepository = folderBaseRepository;
    this.pathSubsitutionService = pathSubsitutionService;
  }

  public void processOrder(OutputOrder order) {
    YmlOutputTemplate ymlOutputTemplate = order.getYmlOutputTemplate();
    FolderBase outputFolderBase = folderBaseRepository.findByPathAndFromOutput(order.getBaseOutputPath().toString(), true);
    List<String> allShaSums = fileRepository.findRequiredOutputShaSums(ymlOutputTemplate.getTypes());

    List<String> moves = fileRepository.findMoveShaSums(allShaSums, outputFolderBase);
    List<String> copies = allShaSums.stream().filter(sha -> !moves.contains(sha)).collect(Collectors.toList());
    logger.info("All sha sums {}", Arrays.toString(allShaSums.toArray(new String[]{})));
    logger.info("Would move sha sums {}", Arrays.toString(moves.toArray(new String[]{})));
    logger.info("Would copy sha sums {}", Arrays.toString(copies.toArray(new String[]{})));
    ArrayList<OutputStep> steps = new ArrayList<>();
    for (String copy:copies) {
      steps.add(prepareCopyStep(copy, order));
    }
    for (String move:moves) {
      steps.add(prepareMoveStep(move, outputFolderBase, order));
    }
    for (OutputStep step:steps) {
      // TODO step complete event
      step.execute();
    }
    // TODO order complete event
  }

  public OutputStep prepareCopyStep(String shaShum, OutputOrder order) {
    File copyFile = fileRepository.findTopBySha256OrderByEarliestKnownDateDesc(shaShum);
    Path from = Paths.get(copyFile.getPath());
    Path to = pathSubsitutionService.constructTargetOutputPath(shaShum, from, order);
    return new CopyStep(from, to);
  }

  public OutputStep prepareMoveStep(String shaShum, FolderBase folderBase, OutputOrder order) {
    File moveFile = fileRepository.findFileFileForMove(shaShum, folderBase);
    Path from = Paths.get(moveFile.getPath());
    Path to = pathSubsitutionService.constructTargetOutputPath(shaShum, from, order);
    return new MoveStep(from, to);
  }
}
