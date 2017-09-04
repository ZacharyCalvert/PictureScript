package com.zachcalvert.picturescript.service;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class FileExtensionExtractorService {

  public String getExtension(File file) {
    return standardizeCaseFileExtension(FilenameUtils.getExtension(file.getAbsolutePath()));
  }

  public String getExtension(Path path) {
    return getExtension(path.toFile());
  }

  public String standardizeCaseFileExtension(String extension) {
    return StringUtils.lowerCase(extension);
  }
}
