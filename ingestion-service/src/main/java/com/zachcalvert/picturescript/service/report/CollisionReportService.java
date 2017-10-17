package com.zachcalvert.picturescript.service.report;

import com.zachcalvert.picturescript.repository.FileRepository;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollisionReportService {

  private static final Logger logger = LoggerFactory.getLogger(CollisionReportService.class);

  private FileRepository fileRepository;

  @Autowired
  public CollisionReportService(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  @PreDestroy
  public void logCollisionsFound() {
    int inputTotal = fileRepository.findTotalInputFiles();
    int distinctTotal = fileRepository.findDistinctOutputShaSums();
    logger.info("Total input files of {} with distinct sha sums found {}, for a total of {} collisions", inputTotal, distinctTotal, inputTotal - distinctTotal);
  }
}
