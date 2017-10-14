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

  public FolderBase(String path, boolean fromOutput) {
    this.path = path;
    this.fromOutput = fromOutput;
  }

  @Id
  @GeneratedValue
  private long id;

  /**
   * The folder's directory path (the start of the ingestion crawl for input)
   */
  @Column(nullable = false, unique = true, updatable = false)
  private String path;

  /**
   * Whether or not the folder is from an output directory.  If from an output directory,
   * the associated files should NOT be distributed for order processing.
   */
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
