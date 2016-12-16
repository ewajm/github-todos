package com.epicodus.githubtodos.models;

import com.epicodus.githubtodos.Constants;

import org.parceler.Parcel;

@Parcel
public class Repo {
    String name;
    String description;
    String language;
    String url;
    String pushId;
    String index;

    public Repo(String name, String description, String language, String url) {
        this.name = name;
        this.description = description;
        this.language = language;
        this.url = url;
        this.index = Constants.UNSPECIFIED_INDEX;
    }

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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

}
