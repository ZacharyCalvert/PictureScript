package com.zachcalvert.PictureScript.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;
import java.util.List;

@ConfigurationProperties("load")
public class LoadConfiguration {
    private List<String> directories;

    public List<String> getDirectories() {
        return directories;
    }

    public void setDirectories(List<String> directories) {
        this.directories = directories;
    }
}
