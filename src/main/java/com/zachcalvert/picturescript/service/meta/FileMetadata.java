package com.zachcalvert.picturescript.service.meta;

import java.time.Instant;
import java.util.Date;

/**
 * Interface for the extracted meta data about processed input files
 */
public interface FileMetadata {

    /**
     * Get the date the file was created according to the file's timestamp
     *
     * @return
     */
    Instant getFileDateCreated();

    /**
     * Based on the file's available metadata, exif information, creation date, date modified, etc, return the
     * earliest date
     *
     * @return  The earliest known date associated to the file, hopefully the date the photo/video/image was taken
     */
    Instant getEarliestMetaDate();
}
