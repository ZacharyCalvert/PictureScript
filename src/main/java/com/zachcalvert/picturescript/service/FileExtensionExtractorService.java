package com.zachcalvert.picturescript.service;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class FileExtensionExtractorService {

  public String getExtension(File file) {
    return StringUtils.lowerCase(FilenameUtils.getExtension(file.getAbsolutePath()));
  }

  public String getExtension(com.zachcalvert.picturescript.model.File file) {
    return StringUtils.lowerCase(FilenameUtils.getExtension(file.getPath()));
  }

  public String getExtension(Path path) {
    return getExtension(path.toFile());
  }
}
