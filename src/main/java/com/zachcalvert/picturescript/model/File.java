package com.zachcalvert.picturescript.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
public class File {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = false, updatable = false)
    private String sha256;

    @Column(nullable = false, unique = true, updatable = false)
    private String path;

    @Column(nullable = false, unique = false, updatable = false)
    private Instant dateCreated;

    @Column(nullable = false, unique = false, updatable = false)
    private Instant earliestKnownDate;

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
}
