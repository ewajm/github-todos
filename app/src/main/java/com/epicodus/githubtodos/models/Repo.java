package com.epicodus.githubtodos.models;

import org.parceler.Parcel;

@Parcel
public class Repo {
    String name;
    String description;
    String language;
    String url;
    String uid;

    public Repo() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public Repo(String name, String description, String language, String url) {
        this.name = name;
        this.description = description;
        this.language = language;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getUrl() {
        return url;
    }
}
