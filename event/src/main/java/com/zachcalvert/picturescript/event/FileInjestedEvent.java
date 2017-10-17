package com.zachcalvert.picturescript.event;

import com.zachcalvert.picturescript.model.File;

public class FileInjestedEvent {
  private File file;

  public FileInjestedEvent(File file) {
    this.file = file;
  }

  public File getFile() {
    return file;
  }
}
