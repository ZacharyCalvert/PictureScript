package com.zachcalvert.picturescript.out.conf;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class used to extract the targets and template definitions for output processing, used for easy
 * YML mapping, but not meant for consumption from services.  See {@link OutputPreprocessor} and
 * {@link OutputOrder}
 */
@ConfigurationProperties("output")
public class YmlOutputConfiguration {

  private List<YmlOutputTemplate> templates;

  private List<YmlOutputTarget> targets;

  public List<YmlOutputTemplate> getTemplates() {
    return templates;
  }

  public void setTemplates(List<YmlOutputTemplate> templates) {
    this.templates = templates;
  }

  public List<YmlOutputTarget> getTargets() {
    return targets;
  }

  public void setTargets(List<YmlOutputTarget> targets) {
    this.targets = targets;
  }
}
