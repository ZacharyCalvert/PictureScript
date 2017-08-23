package com.zachcalvert.PictureScript.processor;

import com.zachcalvert.PictureScript.event.FileDiscoveredEvent;
import com.zachcalvert.PictureScript.model.File;
import com.zachcalvert.PictureScript.repository.FileRepository;
import com.zachcalvert.PictureScript.service.ShaSumCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;


@Component
public class ImageFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileProcessor.class);

    private ShaSumCalculator shaSumCalculator;

    private FileRepository fileRepository;

    @Autowired
    public ImageFileProcessor(ShaSumCalculator shaSumCalculator, FileRepository fileRepository) {
        this.shaSumCalculator = shaSumCalculator;
        this.fileRepository = fileRepository;
    }

    @EventListener
    public void processFileDiscovery(FileDiscoveredEvent event) {
        String sha256 = null;
        Path filePath = event.getPath();
        try {
            sha256 = shaSumCalculator.sha256(filePath.toFile());
            logger.info("SHA256: {} : {}", event.getPath().toString(), sha256);
        } catch (Exception e) {
            logger.error("Error processing sha256", e);
        }
        File file = new File();
        file.setPath(filePath.toAbsolutePath().toString());
        file.setSha256(sha256);
        fileRepository.save(file);
    }
}
