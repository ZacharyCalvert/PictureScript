package com.zachcalvert.picturescript.out.conf;

/**
 * Class meant for extracting target directory configuration straight from YML
 */
public class YmlOutputTarget {

  private String template;

  private String directory;

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getDirectory() {
    return directory;
  }

  public void setDirectory(String directory) {
    this.directory = directory;
  }
}
