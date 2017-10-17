package com.zachcalvert.picturescript.service.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class PathService {

  private static final String CLASSPATH_PREFIX = "classpath:";

  private static final Logger logger = LoggerFactory.getLogger(PathService.class);

  public Path getPath(String path) throws IOException {
    Path result;
    if (StringUtils.startsWith(path, "classpath:")) {
      try {
        ClassPathResource resource = new ClassPathResource(
            StringUtils.removeStart(path, CLASSPATH_PREFIX));
        result = Paths.get(resource.getURI());
      } catch (FileNotFoundException e) {
        logger.warn("Failed to find path as class path resource for {}, will instead use loader - resource potentially does not exist.  Error message: {}", path, e.getMessage());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String classLoaderPath = classLoader.getResource(".").getPath();
        result = Paths.get(classLoaderPath, StringUtils.removeStart(path, CLASSPATH_PREFIX));
      }
    } else {
      result = Paths.get(path);
    }
    return result;
  }
}
