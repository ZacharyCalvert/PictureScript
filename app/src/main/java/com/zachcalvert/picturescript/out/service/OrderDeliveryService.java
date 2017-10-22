package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.service.ExportService;
import com.zachcalvert.picturescript.export.substitution.PathSubsitutionService;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OrderDeliveryService {

  private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryService.class);

  private final FileRepository fileRepository;

  private final ExportService exportService;

  @Autowired
  public OrderDeliveryService(FileRepository fileRepository,
      ExportService exportService) {
    this.fileRepository = fileRepository;
    this.exportService = exportService;
  }

  public void processOrder(OutputOrder order) {
    YmlOutputTemplate ymlOutputTemplate = order.getYmlOutputTemplate();
    List<String> allShaSums = fileRepository.findRequiredOutputShaSums(ymlOutputTemplate.getTypes());
    exportService.processExport(convertOrder(order, allShaSums));
  }

  private ExportRequest convertOrder(OutputOrder order, List<String> allShaSums) {
    ExportRequest request = new ExportRequest();
    request.setBaseOutputPath(order.getBaseOutputPath());
    request.setPathFormats(order.getYmlOutputTemplate().getFormat());
    request.setShaSumIds(allShaSums);
    return request;
  }
}
