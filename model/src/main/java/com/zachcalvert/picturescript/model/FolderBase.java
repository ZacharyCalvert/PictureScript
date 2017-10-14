package com.zachcalvert.picturescript.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FolderBase {

  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false, unique = true, updatable = false)
  private String path;

  @Column(nullable = false, unique = false, updatable = false)
  private boolean fromOutput;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean isFromOutput() {
    return fromOutput;
  }

  public void setFromOutput(boolean fromOutput) {
    this.fromOutput = fromOutput;
  }
}
