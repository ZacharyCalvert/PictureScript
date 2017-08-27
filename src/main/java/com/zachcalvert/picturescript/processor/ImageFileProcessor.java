package com.zachcalvert.picturescript.processor;

import com.zachcalvert.picturescript.event.FileDiscoveredEvent;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.service.ShaSumCalculator;
import com.zachcalvert.picturescript.service.meta.FileMetadata;
import com.zachcalvert.picturescript.service.meta.MetadataExtractor;
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

    private MetadataExtractor metadataExtractor;

    @Autowired
    public ImageFileProcessor(ShaSumCalculator shaSumCalculator,
                              FileRepository fileRepository,
                              MetadataExtractor metadataExtractor) {
        this.shaSumCalculator = shaSumCalculator;
        this.fileRepository = fileRepository;
        this.metadataExtractor = metadataExtractor;
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

        FileMetadata fileMetadata = metadataExtractor.extractMetaData(event.getPath().toFile());
        file.setDateCreated(fileMetadata.getFileDateCreated());
        file.setEarliestKnownDate(fileMetadata.getEarliestMetaDate());

        fileRepository.save(file);
    }
}
