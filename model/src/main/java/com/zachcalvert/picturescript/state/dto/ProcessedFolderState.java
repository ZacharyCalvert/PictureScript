package com.zachcalvert.picturescript.state.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zachcalvert.picturescript.state.dto.ProcessedFile;
import java.util.List;

/**
 * JSON DTO representing the folder state, useful for persisting a representation of the state of
 * the folder as exported.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedFolderState {

  /**
   * File name, to be stored in the base of the export folder
   */
  public static final String DEFAULT_FILE_NAME = ".ps_state";

  private List<ProcessedFile> processedFiles;

  public List<ProcessedFile> getProcessedFiles() {
    return processedFiles;
  }

  public void setProcessedFiles(
      List<ProcessedFile> processedFiles) {
    this.processedFiles = processedFiles;
  }
}
