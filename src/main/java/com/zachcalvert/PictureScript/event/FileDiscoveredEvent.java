package com.zachcalvert.PictureScript.event;

import java.io.File;

public class FileDiscoveredEvent {

    private File file;

    public FileDiscoveredEvent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
