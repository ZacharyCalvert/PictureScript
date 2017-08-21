package com.zachcalvert.PictureScript.loader;

import com.zachcalvert.PictureScript.conf.LoadConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by Zach on 8/20/17.
 */
@Component
public class BaseDirLoader {

    private static final Logger logger = LoggerFactory.getLogger(BaseDirLoader.class);

    private LoadConfiguration loadConfiguration;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BaseDirLoader(LoadConfiguration loadConfiguration, ApplicationEventPublisher applicationEventPublisher) {
        this.loadConfiguration = loadConfiguration;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void processDirectories() {
        loadConfiguration.getDirectories().stream().forEach((dir) -> {
            logger.debug("Directory configured for processing: " + dir);
            File f = Paths.get(dir).toFile();
            logger.debug(f.getAbsolutePath());

                }
        );
    }
}
