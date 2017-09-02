package com.zachcalvert.picturescript.event;

import com.zachcalvert.picturescript.model.FolderBase;
import java.nio.file.Path;

public class FileDiscoveredEvent {

    private Path path;

    private FolderBase folderBase;

    public FileDiscoveredEvent(Path path, FolderBase folderBase) {
        this.path = path;
        this.folderBase = folderBase;
    }

    public Path getPath() {
        return path;
    }

    public FolderBase getFolderBase() {
        return folderBase;
    }
}
