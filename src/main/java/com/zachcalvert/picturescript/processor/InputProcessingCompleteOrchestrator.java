package com.zachcalvert.picturescript.processor;

import com.zachcalvert.picturescript.event.FileDiscoveredEvent;
import com.zachcalvert.picturescript.event.FileDiscoveryCompleteEvent;
import com.zachcalvert.picturescript.event.FileInjestedEvent;
import com.zachcalvert.picturescript.event.InputProcessingCompleteEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InputProcessingCompleteOrchestrator {

  private static final Logger logger = LoggerFactory.getLogger(InputProcessingCompleteOrchestrator.class);

  private AtomicBoolean receivedDiscoveryCompleteEvent = new AtomicBoolean(false);
  private AtomicInteger filesProcessing = new AtomicInteger(0);

  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public InputProcessingCompleteOrchestrator(
      ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @EventListener
  public void receiveEvent(FileDiscoveredEvent event) {
    filesProcessing.incrementAndGet();
    sendProcessingCompleteIfReady();
  }

  @EventListener
  public void receiveEvent(FileInjestedEvent event) {
    filesProcessing.decrementAndGet();
    sendProcessingCompleteIfReady();
  }

  @EventListener
  public void receiveEvent(FileDiscoveryCompleteEvent event) {
    logger.info("Discovery complete");
    receivedDiscoveryCompleteEvent.set(true);
    sendProcessingCompleteIfReady();
  }

  private void sendProcessingCompleteIfReady() {
    if (receivedDiscoveryCompleteEvent.get() && filesProcessing.get() == 0) {
      logger.info("Input processing complete, signalling input processing complete.");
      applicationEventPublisher.publishEvent(new InputProcessingCompleteEvent());
    }
  }

}
