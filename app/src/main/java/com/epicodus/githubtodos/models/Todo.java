package com.epicodus.githubtodos.models;

import org.parceler.Parcel;

import java.sql.Date;

@Parcel
public class Todo {
    String title;
    String body;
    String url;
    int urgency;
    int difficulty;
    String created;
    String notes;
    boolean toDone;
    String pushId;
    String repoId;

    public Todo(String title, String body, int urgency, int difficulty) {
        this.title = title;
        this.body = body;
        this.urgency = urgency;
        this.difficulty = difficulty;
        Date date = new Date(System.currentTimeMillis());
        this.created = date.toString();
        this.toDone = false;
    }

    public Todo() {
    }

    public Todo(String title, String body, String url, String created) {
        this.title = title;
        this.body = body;
        this.url = url;
        this.created = created;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isToDone() {
        return toDone;
    }

    public void setToDone(boolean toDone) {
        this.toDone = toDone;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public int getUrgency() {
        return urgency;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getCreated() {
        return created;
    }

    public String getNotes() {
        return notes;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
