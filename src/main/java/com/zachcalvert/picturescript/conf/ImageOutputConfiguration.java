package com.zachcalvert.picturescript.conf;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("output.image")
public class ImageOutputConfiguration {

  private List<String> ignore;

  private List<String> types;

  private String format;

  public List<String> getIgnore() {
    return ignore;
  }

  public void setIgnore(List<String> ignore) {
    this.ignore = ignore;
  }

  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}
