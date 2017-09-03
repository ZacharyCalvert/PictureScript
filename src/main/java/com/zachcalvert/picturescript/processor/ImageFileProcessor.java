package com.zachcalvert.picturescript.processor;

import com.zachcalvert.picturescript.event.FileDiscoveredEvent;
import com.zachcalvert.picturescript.event.FileInjestedEvent;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.service.FileExtensionExtractorService;
import com.zachcalvert.picturescript.service.ShaSumCalculator;
import com.zachcalvert.picturescript.service.meta.FileMetadata;
import com.zachcalvert.picturescript.service.meta.MetadataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;


@Component
public class ImageFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileProcessor.class);

    private final ShaSumCalculator shaSumCalculator;

    private final FileRepository fileRepository;

    private final MetadataExtractor metadataExtractor;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final FileExtensionExtractorService fileExtensionExtractorService;

    @Autowired
    public ImageFileProcessor(
            ShaSumCalculator shaSumCalculator,
            FileRepository fileRepository,
            MetadataExtractor metadataExtractor,
            ApplicationEventPublisher applicationEventPublisher,
            FileExtensionExtractorService fileExtensionExtractorService) {
        this.shaSumCalculator = shaSumCalculator;
        this.fileRepository = fileRepository;
        this.metadataExtractor = metadataExtractor;
        this.applicationEventPublisher = applicationEventPublisher;
        this.fileExtensionExtractorService = fileExtensionExtractorService;
    }

    @EventListener
    public void processFileDiscovery(FileDiscoveredEvent event) {

        File file = new File();
        try {
            String sha256 = null;
            Path filePath = event.getPath();
            try {
                sha256 = shaSumCalculator.sha256(filePath.toFile());
                logger.info("SHA256: {} : {}", event.getPath().toString(), sha256);
            } catch (Exception e) {
                logger.error("Error processing sha256", e);
            }
            file.setPath(filePath.toAbsolutePath().toString());
            file.setSha256(sha256);

            FileMetadata fileMetadata = metadataExtractor.extractMetaData(event.getPath().toFile());
            file.setDateCreated(fileMetadata.getFileDateCreated());
            file.setEarliestKnownDate(fileMetadata.getEarliestMetaDate());
            file.setFolderBase(event.getFolderBase());
            file.setExtension(fileExtensionExtractorService.getExtension(filePath));

            fileRepository.save(file);
        } finally {
            applicationEventPublisher.publishEvent(new FileInjestedEvent(file));
        }
    }
}
