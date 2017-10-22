package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.event.InputProcessingCompleteEvent;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.service.report.ReportService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OutputProcessReadyMonitorService {

  private static final Logger logger = LoggerFactory.getLogger(OutputProcessReadyMonitorService.class);

  private List<OutputOrder> outputOrders;

  private OrderDeliveryService orderDeliveryService;

  private List<ReportService> reports;

  @Autowired
  public OutputProcessReadyMonitorService(
      List<OutputOrder> outputOrders,
      OrderDeliveryService orderDeliveryService,
      Optional<List<ReportService>> reportServices) {
    this.outputOrders = outputOrders;
    this.orderDeliveryService = orderDeliveryService;
    this.reports = reportServices.orElseGet(ArrayList::new);
  }

  @EventListener
  public void receiveEvent(InputProcessingCompleteEvent event) {
    logger.info("Input processing event complete.  Beginning output processing of {} output orders.", outputOrders.size());
    // TODO Thread me
    for (OutputOrder order:outputOrders) {
      orderDeliveryService.processOrder(order);
    }
    this.reports.stream().forEach(reportService -> reportService.logReport());
  }
}
