package com.epicodus.githubtodos.models;

/**
 * Created by Ewa on 12/2/2016.
 */
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

}
