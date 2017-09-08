package com.zachcalvert.picturescript.out.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrittenFile {

    private String sha256;

    private String relativePath;

    private Instant dateCreated;

    private Instant earliestKnownDate;

    private String extension;

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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
