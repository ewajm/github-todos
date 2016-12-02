package com.epicodus.githubtodos.models;

import org.parceler.Parcel;

@Parcel
public class Repo {
    String mName;
    String description;
    String language;
    String url;

    public Repo() {
    }

    public Repo(String name, String description, String language, String url) {
        mName = name;
        this.description = description;
        this.language = language;
        this.url = url;
    }

    public String getName() {
        return mName;
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
