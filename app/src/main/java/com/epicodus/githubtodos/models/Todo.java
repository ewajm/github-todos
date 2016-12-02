package com.epicodus.githubtodos.models;

import org.parceler.Parcel;

@Parcel
public class Todo {
    String mTitle;
    String mBody;
    String mUrl;
    int mUrgency;
    int mDifficulty;
    String mCreated;
    String mNotes;

    public void setTitle(String title) {
        mTitle = title;
    }

    public Todo() {
    }

    public Todo(String title, String body, String url, String created) {
        mTitle = title;
        mBody = body;
        mUrl = url;
        mCreated = created;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getUrgency() {
        return mUrgency;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public String getCreated() {
        return mCreated;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setUrgency(int urgency) {
        mUrgency = urgency;
    }

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }
}
