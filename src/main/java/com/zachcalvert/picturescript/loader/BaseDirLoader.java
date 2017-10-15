package com.zachcalvert.picturescript.loader;

import com.zachcalvert.picturescript.conf.LoadConfiguration;
import com.zachcalvert.picturescript.event.FileDiscoveredEvent;
import com.zachcalvert.picturescript.event.FileDiscoveryCompleteEvent;
import com.zachcalvert.picturescript.event.FileInjestedEvent;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import com.zachcalvert.picturescript.service.ingest.DirectoryIngestionService;
import com.zachcalvert.picturescript.service.ingest.IngestionListener;
import com.zachcalvert.picturescript.service.util.PathService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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
        applicationEventPublisher.publishEvent(new FileDiscoveryCompleteEvent());
    }
}
