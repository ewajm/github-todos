package com.epicodus.githubtodos.services;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.models.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        urlBuilder.addQueryParameter("sort", "updated");
        String url = urlBuilder.toString();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Repo> processRepoResponse(Response response){
        ArrayList<Repo> repoList = new ArrayList<>();
        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONArray jsonRepoArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonRepoArray.length(); i++) {
                    JSONObject repoJSON = jsonRepoArray.getJSONObject(i);
                    String name = repoJSON.getString("name");
                    String language = !repoJSON.getString("language").equals("null")? repoJSON.getString("language"): "No language specified";
                    String url = repoJSON.getString("url");
                    String description = !repoJSON.getString("description").equals("null") ? repoJSON.getString("description"): "No description provided";

                    Repo repo = new Repo(name, description, language, url);
                    repoList.add(repo);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return repoList;
    }
}
