package com.zachcalvert.picturescript.state.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import java.time.Instant;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedFile {

  private String sha256;

  private String relativePath;

  private Date dateCreated;

  private Date earliestKnownDate;

  private String extension;

  private String originalFileName;

  public ProcessedFile() {}

  public ProcessedFile(FolderBase folderBase, File file) {
    this.sha256 = file.getSha256();
    this.relativePath = StringUtils.removeStart(file.getPath(), folderBase.getPath());
    this.dateCreated = Date.from(file.getDateCreated());
    this.earliestKnownDate = Date.from(file.getEarliestKnownDate());
    this.extension = file.getExtension();
    this.originalFileName = file.getOriginalFileName();
  }

  public String getSha256() {
    return sha256;
  }

  public void setSha256(String sha256) {
    this.sha256 = sha256;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getEarliestKnownDate() {
    return earliestKnownDate;
  }

  public void setEarliestKnownDate(Date earliestKnownDate) {
    this.earliestKnownDate = earliestKnownDate;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getOriginalFileName() {
    return originalFileName;
  }

  public void setOriginalFileName(String originalFileName) {
    this.originalFileName = originalFileName;
  }
}
