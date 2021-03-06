package com.zachcalvert.picturescript.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(
    indexes = {
        @Index(columnList = "sha256", name = "idx_file_sha256")
    }
)
public class File {

    public File() {
    }

    public File(boolean availableForExport, String sha256, String path, Instant dateCreated, Instant earliestKnownDate,
        String extension, FolderBase folderBase, String originalFileName) {
        this.availableForExport = availableForExport;
        this.sha256 = sha256;
        this.path = path;
        this.dateCreated = dateCreated;
        this.earliestKnownDate = earliestKnownDate;
        this.extension = extension;
        this.folderBase = folderBase;
        this.originalFileName = originalFileName;
    }

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = false, updatable = true)
    private boolean availableForExport;

    @Column(nullable = false, unique = false, updatable = false)
    private String sha256;

    @Column(nullable = false, unique = true, updatable = false)
    private String path;

    @Column(nullable = false, unique = false, updatable = false)
    private Instant dateCreated;

    @Column(nullable = false, unique = false, updatable = false)
    private Instant earliestKnownDate;

    @Column(nullable = true, unique = false, updatable = false)
    private String extension;

    @Column(nullable = false, unique = false, updatable = false)
    private String originalFileName;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private FolderBase folderBase;

    public long getId() {
        return id;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getEarliestKnownDate() {
        return earliestKnownDate;
    }

    public void setEarliestKnownDate(Instant earliestKnownDate) {
        this.earliestKnownDate = earliestKnownDate;
    }

    public FolderBase getFolderBase() {
        return folderBase;
    }

    public void setFolderBase(FolderBase folderBase) {
        this.folderBase = folderBase;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isAvailableForExport() {
        return availableForExport;
    }

    public void setAvailableForExport(boolean availableForExport) {
        this.availableForExport = availableForExport;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
