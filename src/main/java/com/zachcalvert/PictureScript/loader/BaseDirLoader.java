package com.zachcalvert.PictureScript.loader;

import com.zachcalvert.PictureScript.conf.LoadConfiguration;
import com.zachcalvert.PictureScript.event.FileDiscoveredEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

/**
 * Created by Zach on 8/20/17.
 */
@Component
public class BaseDirLoader {

    private static final String CLASSPATH_PREFIX = "classpath:";

    private static final Logger logger = LoggerFactory.getLogger(BaseDirLoader.class);

    private LoadConfiguration loadConfiguration;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BaseDirLoader(LoadConfiguration loadConfiguration, ApplicationEventPublisher applicationEventPublisher) {
        this.loadConfiguration = loadConfiguration;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener
    public void processDirectories(final ApplicationReadyEvent applicationReadyEvent) {
        loadConfiguration.getDirectories().stream().forEach((dir) -> {
            logger.debug("Directory configured for processing: " + dir);


            try {
                Path dirPath;
                if (StringUtils.startsWith(dir, "classpath:")) {
                    ClassPathResource resource = new ClassPathResource(StringUtils.removeStart(dir, CLASSPATH_PREFIX));
                    dirPath = Paths.get(resource.getURI());
                } else {
                    dirPath = Paths.get(dir);
                }

                Files.walkFileTree(dirPath, new FileFinder());
            } catch (Exception e) {
                logger.error("Error loading directory: " + dir, e);
            }
        });
    }

    private class FileFinder extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (attrs.isRegularFile()) {
                logger.info("File found: " + file.toAbsolutePath());
                FileDiscoveredEvent event = new FileDiscoveredEvent(file);
                applicationEventPublisher.publishEvent(event);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
