package com.zachcalvert.picturescript.loader;

import com.zachcalvert.picturescript.conf.LoadConfiguration;
import com.zachcalvert.picturescript.event.InputProcessingCompleteEvent;
import com.zachcalvert.picturescript.service.ingest.DirectoryIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by Zach on 8/20/17.
 */
@Component
public class BaseDirLoader {

    private static final Logger logger = LoggerFactory.getLogger(BaseDirLoader.class);

    private final LoadConfiguration loadConfiguration;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final DirectoryIngestionService directoryIngestionService;

    @Autowired
    public BaseDirLoader(LoadConfiguration loadConfiguration,
        ApplicationEventPublisher applicationEventPublisher,
        DirectoryIngestionService directoryIngestionService) {
        this.loadConfiguration = loadConfiguration;
        this.applicationEventPublisher = applicationEventPublisher;
        this.directoryIngestionService = directoryIngestionService;
    }

    @EventListener
    public void processDirectories(final ApplicationReadyEvent applicationReadyEvent) {
        loadConfiguration.getDirectories().stream().forEach((dir) -> {
            logger.debug("Directory configured for processing: " + dir);
            directoryIngestionService.processDirectory(true, dir, loadConfiguration.getIgnore());
        });
        applicationEventPublisher.publishEvent(new InputProcessingCompleteEvent());
    }
}
