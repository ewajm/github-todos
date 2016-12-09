package com.epicodus.githubtodos.models;

import org.parceler.Parcel;

@Parcel
public class Repo {
    String mName;
    String mDescription;
    String mLanguage;
    String mUrl;

    public Repo() {
    }

    public Repo(String name, String description, String language, String url) {
        mName = name;
        mDescription = description;
        mLanguage = language;
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getUrl() {
        return mUrl;
    }
}
