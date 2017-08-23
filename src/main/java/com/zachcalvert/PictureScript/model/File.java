package com.zachcalvert.PictureScript.model;

import javax.persistence.*;

@Entity
public class File {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = false, updatable = false)
    private String sha256;

    @Column(nullable = false, unique = true, updatable = false)
    private String path;

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
}
