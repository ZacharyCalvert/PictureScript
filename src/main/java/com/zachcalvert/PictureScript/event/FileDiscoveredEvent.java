package com.zachcalvert.picturescript.event;

import org.springframework.context.ApplicationEvent;

import java.io.File;
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
