package com.zachcalvert.picturescript.service.meta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@Service
public class DrewExifExtractor implements MetadataExtractor {

    private static final Logger logger = LoggerFactory.getLogger(DrewExifExtractor.class);

    @Override
    public FileMetadata extractMetaData(File file) {
        logger.debug("Processing meta data for " + file.getAbsolutePath());
        if (!file.exists()) {
            throw new RuntimeException("File requested extract meta data does not exist: " + file.getAbsolutePath());
        }

        Instant dateCreated = null;
        Instant earliestKnownDate = null;

        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            dateCreated = attr.creationTime().toInstant();
            earliestKnownDate = getEarliestInstant(dateCreated, attr.lastAccessTime().toInstant(), attr.lastModifiedTime().toInstant()).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Could not get basic file attributes for " + file.getAbsolutePath(), e);
        }

        try {
            ImageMetadataReader.readMetadata(file);
        } catch (Exception e) {
            logger.warn("Error extracting file meta data for {}, due to {}", file.getAbsolutePath(), e.getMessage());
        }

        logger.info("Date created for {} is {} with earliest date touch point {}", file.getAbsolutePath(), dateCreated, earliestKnownDate);
        return new ExifData(dateCreated, earliestKnownDate);
    }

    protected static Optional<Instant> getEarliestInstant(Instant... times) {
        return Arrays
                .stream(times)
                .filter(instant -> instant != null)
                .min(new Comparator<Instant>() {
                    @Override
                    public int compare(Instant o1, Instant o2) {
                        return o1.compareTo(o2);
                    }
                });
    }

    private static class ExifData implements FileMetadata {
        private Instant dateCreated;

        private Instant earliestKnownDate;

        public ExifData(Instant dateCreated, Instant earliestKnownDate) {
            this.dateCreated = dateCreated;
            this.earliestKnownDate = earliestKnownDate;
        }

        @Override
        public Instant getFileDateCreated() {
            return dateCreated;
        }

        @Override
        public Instant getEarliestMetaDate() {
            return earliestKnownDate;
        }
    }
}
