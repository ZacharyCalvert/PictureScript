package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.out.conf.YmlOutputConfiguration;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.service.report.ReportService;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuspectTypeReportingService implements ReportService {

  private static final Logger logger = LoggerFactory.getLogger(SuspectTypeReportingService.class);

  private final FileExtensionExtractorService fileExtensionExtractorService;

  private final YmlOutputConfiguration ymlOutputConfiguration;

  private final FileRepository fileRepository;

  @Autowired
  public SuspectTypeReportingService(
      FileExtensionExtractorService fileExtensionExtractorService,
      YmlOutputConfiguration ymlOutputConfiguration,
      FileRepository fileRepository) {
    this.fileExtensionExtractorService = fileExtensionExtractorService;
    this.ymlOutputConfiguration = ymlOutputConfiguration;
    this.fileRepository = fileRepository;
  }

  @Override
  public void logReport() {
    List<String> extensions = fileRepository.findAllFileExtensions();
    HashSet<String> templateCovered = new HashSet<>();
    ymlOutputConfiguration.getTemplates().stream().forEach(conf -> conf.getTypes().stream().map(fileExtensionExtractorService::standardizeCaseFileExtension).forEach(type -> templateCovered.add(type)));
    HashSet<String> notCovered = new HashSet<>();
    extensions.stream().filter(ext -> !templateCovered.contains(ext)).forEach(ext -> notCovered.add(ext));
    logger.info("File types not covered by template: " + sortAndStringifyExtensionSet(notCovered));
  }

  private String sortAndStringifyExtensionSet(HashSet<String> set) {
    String[] setArray = set.toArray(new String[set.size()]);
    Arrays.sort(setArray);
    return Arrays.toString(setArray);
  }
}

