package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.conf.YmlOutputTemplate;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
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

  @Autowired
  public OrderDeliveryService(FileRepository fileRepository,
      FolderBaseRepository folderBaseRepository) {
    this.fileRepository = fileRepository;
    this.folderBaseRepository = folderBaseRepository;
  }

  public void processOrder(OutputOrder order) {
    YmlOutputTemplate ymlOutputTemplate = order.getYmlOutputTemplate();
    FolderBase outputFolderBase = folderBaseRepository.findByPathAndFromOutput(order.getTarget(), true);
    List<String> allShaSums = fileRepository.findRequiredOutputShaSums(ymlOutputTemplate.getTypes());

    List<String> moves = fileRepository.findMoveShaSums(allShaSums, outputFolderBase);
    List<String> copies = allShaSums.stream().filter(sha -> !moves.contains(sha)).collect(Collectors.toList());
    logger.info("All sha sums {}", Arrays.toString(allShaSums.toArray(new String[]{})));
    logger.info("Would move sha sums {}", Arrays.toString(moves.toArray(new String[]{})));
    logger.info("Would copy sha sums {}", Arrays.toString(copies.toArray(new String[]{})));
  }
}
