package com.zachcalvert.picturescript.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * FolderBase is the representation of an ingested folder, either for input processing
 * or output updating.  In a situation where the same folder is used for input as for
 * output, such as a re-arrangement of image files, there will be a copy of the folder
 * base with different flags set for input and output processing.
 */
@Entity
public class FolderBase {

  public FolderBase() {
  }

  public FolderBase(String path) {
    this.path = path;
  }

  @Id
  @GeneratedValue
  private long id;

  /**
   * The folder's directory path (the start of the ingestion crawl for input)
   */
  @Column(nullable = false, unique = true, updatable = false)
  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
