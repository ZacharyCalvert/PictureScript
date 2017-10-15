package com.zachcalvert.picturescript.processor;

import com.drew.imaging.FileType;
import com.zachcalvert.picturescript.event.FileDiscoveredEvent;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class FileTypeProcessor {

    HashSet<String> fileTypesProcssed = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(FileType.class);

    private FileExtensionExtractorService fileExtensionExtractorService;

    @Autowired
    public FileTypeProcessor(
        FileExtensionExtractorService fileExtensionExtractorService) {
        this.fileExtensionExtractorService = fileExtensionExtractorService;
    }

    @EventListener
    public void processFileDiscovery(FileDiscoveredEvent event) {

        String extension = fileExtensionExtractorService.getExtension(event.getPath());
        synchronized (fileTypesProcssed) {
            if (!fileTypesProcssed.contains(extension)) {
                logger.info("New file type found: " + extension);
                fileTypesProcssed.add(extension);
            }
        }
    }

    @PreDestroy
    public void logTypesFound() {
        String[] allTypes = fileTypesProcssed.toArray(new String[fileTypesProcssed.size()]);
        Arrays.sort(allTypes);
        logger.info("File types found: " + Arrays.toString(allTypes));
    }
}
