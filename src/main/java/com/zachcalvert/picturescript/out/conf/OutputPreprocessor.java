package com.zachcalvert.picturescript.out.conf;

import com.zachcalvert.picturescript.err.OutputTemplateNotFoundException;
import com.zachcalvert.picturescript.service.FileExtensionExtractorService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OutputPreprocessor {

  private final YmlOutputConfiguration ymlOutputConfiguration;

  private final FileExtensionExtractorService fileExtensionExtractorService;

  @Autowired
  public OutputPreprocessor(
      YmlOutputConfiguration ymlOutputConfiguration,
      FileExtensionExtractorService fileExtensionExtractorService) {
    this.ymlOutputConfiguration = ymlOutputConfiguration;
    this.fileExtensionExtractorService = fileExtensionExtractorService;
  }

  @Bean
  public List<OutputOrder> outputOrders() {
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
      result.add(new OutputOrder(target.getDirectory(), template));
    }
    return result;
  }
}
