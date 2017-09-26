package com.zachcalvert.picturescript.loader;

import com.zachcalvert.picturescript.conf.LoadConfiguration;
import com.zachcalvert.picturescript.event.FileDiscoveredEvent;
import com.zachcalvert.picturescript.event.FileDiscoveryCompleteEvent;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import com.zachcalvert.picturescript.service.PathService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
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

    private LoadConfiguration loadConfiguration;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final FolderBaseRepository folderBaseRepository;

    private final PathService pathService;

    @Autowired
    public BaseDirLoader(LoadConfiguration loadConfiguration,
        ApplicationEventPublisher applicationEventPublisher,
        FolderBaseRepository folderBaseRepository,
        PathService pathService) {
        this.loadConfiguration = loadConfiguration;
        this.applicationEventPublisher = applicationEventPublisher;
        this.folderBaseRepository = folderBaseRepository;
        this.pathService = pathService;
    }

    @EventListener
    public void processDirectories(final ApplicationReadyEvent applicationReadyEvent) {
        loadConfiguration.getDirectories().stream().forEach((dir) -> {
            logger.debug("Directory configured for processing: " + dir);

            FolderBase folderBase = new FolderBase();
            folderBase.setFromOutput(false);
            folderBase.setPath(dir);
            folderBaseRepository.save(folderBase);

            try {
                Path dirPath = pathService.getPath(dir);
                logger.debug("Full directory path for input processing: " + dirPath.toString());
                Files.walkFileTree(dirPath, new FileFinder(folderBase));
            } catch (Exception e) {
                logger.error("Error loading directory: " + dir, e);
            }
        });
        applicationEventPublisher.publishEvent(new FileDiscoveryCompleteEvent());
    }

    private boolean isIgnored(Path file) {
        String type = StringUtils.lowerCase(FilenameUtils.getExtension(file.toString()));
        return loadConfiguration.getIgnore().contains(type);
    }

    private class FileFinder extends SimpleFileVisitor<Path> {

        private FolderBase folderBase;

        public FileFinder(FolderBase folderBase) {
            this.folderBase = folderBase;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (attrs.isRegularFile() && ! isIgnored(file)) {
                logger.info("File found: " + file.toAbsolutePath());
                FileDiscoveredEvent event = new FileDiscoveredEvent(file, folderBase);
                applicationEventPublisher.publishEvent(event);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
