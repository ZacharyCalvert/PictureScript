package com.zachcalvert.picturescript.service.meta;

import java.io.File;

public interface MetadataExtractor {

    FileMetadata extractMetaData(File file);
}
