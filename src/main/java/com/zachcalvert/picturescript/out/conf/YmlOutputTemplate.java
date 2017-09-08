package com.zachcalvert.picturescript.out.conf;

import java.util.List;

/**
 * Class meant for extracting template file configuration straight from YML
 */
public class YmlOutputTemplate {

  private String name;

  private List<String> types;

  private String format;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
