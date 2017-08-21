package com.zachcalvert.PictureScript.processor;

import com.zachcalvert.PictureScript.event.FileDiscoveredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

public class ShaSumCalculator {

    private final Logger logger = LoggerFactory.getLogger(ShaSumCalculator.class);

    @EventListener
    public void handleContextRefresh(FileDiscoveredEvent event) {
        logger.debug("Received file discovered event: " + event.getFile());
    }
}
