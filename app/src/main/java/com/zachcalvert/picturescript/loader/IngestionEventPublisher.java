package com.zachcalvert.picturescript.loader;

import com.zachcalvert.picturescript.event.FileInjestedEvent;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.service.ingest.IngestionListener;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class IngestionEventPublisher implements IngestionListener {

  private static final Logger logger = LoggerFactory.getLogger(BaseDirLoader.class);

  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public IngestionEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void fileIgnored(FolderBase folderBase, Path path) {
    logger.debug("Ignoring file {}", path.toAbsolutePath().toString());
  }

  @Override
  public void fileIngested(FolderBase folderBase, Path path, File file) {
    applicationEventPublisher.publishEvent(new FileInjestedEvent(file));
  }
}
