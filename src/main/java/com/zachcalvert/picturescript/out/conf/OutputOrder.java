package com.zachcalvert.picturescript.out.conf;

public class OutputOrder {

  private String target;

  private YmlOutputTemplate ymlOutputTemplate;

  public OutputOrder(String target,
      YmlOutputTemplate ymlOutputTemplate) {
    this.target = target;
    this.ymlOutputTemplate = ymlOutputTemplate;
  }

  public String getTarget() {
    return target;
  }

  public YmlOutputTemplate getYmlOutputTemplate() {
    return ymlOutputTemplate;
  }
}
