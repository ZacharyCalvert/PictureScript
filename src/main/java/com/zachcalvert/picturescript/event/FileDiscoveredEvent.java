package com.zachcalvert.picturescript.event;

import java.nio.file.Path;

public class FileDiscoveredEvent {

    private Path file;

    public FileDiscoveredEvent(Path file) {
        this.file = file;
    }

    public Path getPath() {
        return file;
    }
}
