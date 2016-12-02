package com.epicodus.githubtodos.models;

/**
 * Created by Ewa on 12/2/2016.
 */
public class Repo {
    String mName;
    String description;
    String language;
    String url;

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

    public Repo() {
    }

    public Repo(String name, String description, String language, String url) {
        mName = name;
        this.description = description;
        this.language = language;
        this.url = url;
    }

}
