package com.epicodus.githubtodos;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Ewa on 11/28/2016.
 */

public class GithubService {

    public static void findRepos(String user, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = Constants.GITHUB_BASE_URL + Constants.GITHUB_USER_QUERY_PARAMETER + "/" + user + "/" + Constants.GITHUB_REPO_QUERY_PARAMTER + "?access_token=" + Constants.GITHUB_TOKEN;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
