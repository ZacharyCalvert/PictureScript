package com.zachcalvert.picturescript.out.conf;

import com.zachcalvert.picturescript.err.OutputTemplateNotFoundException;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import com.zachcalvert.picturescript.service.util.PathService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OutputPreprocessor {

  private final YmlOutputConfiguration ymlOutputConfiguration;

  private final FileExtensionExtractorService fileExtensionExtractorService;

  private final PathService pathService;

  private final static Logger logger = LoggerFactory.getLogger(OutputPreprocessor.class);

  @Autowired
  public OutputPreprocessor(
      YmlOutputConfiguration ymlOutputConfiguration,
      FileExtensionExtractorService fileExtensionExtractorService,
      PathService pathService) {
    this.ymlOutputConfiguration = ymlOutputConfiguration;
    this.fileExtensionExtractorService = fileExtensionExtractorService;
    this.pathService = pathService;
  }

  @Bean
  public List<OutputOrder> outputOrders() throws IOException {
    final HashMap<String, YmlOutputTemplate> nameToTemplateMapping = new HashMap<>();
    ymlOutputConfiguration
        .getTemplates().stream().forEach(t -> nameToTemplateMapping.put(t.getName(), t));
    final ArrayList<OutputOrder> result = new ArrayList<>();
    for (YmlOutputTarget target: ymlOutputConfiguration.getTargets()) {
      YmlOutputTemplate template = nameToTemplateMapping.get(target.getTemplate());
      if (template == null) {
        throw new OutputTemplateNotFoundException(String.format("Ouptut template '%s' not found", target.getTemplate()));
      }
      template.setTypes(template.getTypes().stream().map(fileExtensionExtractorService::standardizeCaseFileExtension).collect(Collectors.toList()));
      Path outputPath = pathService.getPath(target.getDirectory());
      logger.info("Output for template {} configured to full path of {}", template.getName(), outputPath.toString());
      result.add(new OutputOrder(outputPath, template, target.isDryRun()));
    }
    return result;
  }
}
