package com.epicodus.githubtodos.services;

import com.epicodus.githubtodos.Constants;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Ewa on 12/2/2016.
 */

public class GithubService {


    public static void getUserRepos(String user, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GITHUB_API_URL).newBuilder();
        urlBuilder.addPathSegment(Constants.GITHUB_USERS_PATH);
        urlBuilder.addPathSegment(user);
        urlBuilder.addPathSegment(Constants.GITHUB_REPOS_PATH);
        urlBuilder.addQueryParameter(Constants.GITHUB_TOKEN_QUERY, Constants.GITHUB_TOKEN);
        String url = urlBuilder.toString();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
