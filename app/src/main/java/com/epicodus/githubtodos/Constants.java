package com.epicodus.githubtodos;

import android.os.Build;

public class Constants {
    public static final String GITHUB_TOKEN = BuildConfig.GITHUB_TOKEN;
    public static final String GITHUB_CLIENT_ID = BuildConfig.GITHUB_CLIENT_ID;
    public static final String GITHUB_CLIENT_SECRET = BuildConfig.GITHUB_CLIENT_SECRET;
    public static final String GITHUB_API_URL = "https://api.github.com";
    public static final String GITHUB_USERS_PATH = "users";
    public static final String GITHUB_REPOS_PATH = "repos";
    public static final String GITHUB_ISSUES_PATH = "issues";
    public static final String GITHUB_TOKEN_QUERY = "access_token";
    public static final String PREFERENCES_USERNAME_KEY = "username";
    public static final String PREFERENCES_GITHUB_TOGGLE_KEY = "github";
    public static final String FIREBASE_TODOS_REFERENCE = "todos";
    public static final String FIREBASE_REPOS_REFERENCE = "repos";
}
