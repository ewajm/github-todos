package com.epicodus.githubtodos.services;

import android.util.Log;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.models.User;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

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
        urlBuilder.addQueryParameter("sort", "pushed");
        String url = urlBuilder.toString();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getUserToken(String username, String password, Callback callback){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id", Constants.GITHUB_CLIENT_ID);
        params.put("client_secret", Constants.GITHUB_CLIENT_SECRET);
        params.put("note", "testing");
        JSONObject parameter = new JSONObject(params);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BasicAuthIntercepter(username, password)).build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GITHUB_API_URL).newBuilder();
        urlBuilder.addPathSegment("authorizations");
        String url = urlBuilder.toString();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Log.d("GithubService", "getUserToken: " + url);
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    //limit to only issues created by owner?
    public static void getRepoIssues(String repoName, String username, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GITHUB_API_URL).newBuilder();
        urlBuilder.addPathSegment(Constants.GITHUB_REPOS_PATH);
        urlBuilder.addPathSegment(username);
        urlBuilder.addPathSegment(repoName);
        urlBuilder.addPathSegment(Constants.GITHUB_ISSUES_PATH);
        urlBuilder.addQueryParameter(Constants.GITHUB_TOKEN_QUERY, Constants.GITHUB_TOKEN);
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
                    String url = repoJSON.getString("html_url");
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

    public ArrayList<Todo> processIssueResponse(Response response){
        ArrayList<Todo> todoList = new ArrayList<>();
        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONArray jsonTodoArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonTodoArray.length(); i++) {
                    JSONObject todoJSON = jsonTodoArray.getJSONObject(i);
                    String title = todoJSON.getString("title");
                    if(title.contains("TODO")){
                        String body = !todoJSON.getString("body").equals("null")? todoJSON.getString("body"): "No body specified";
                        String url = todoJSON.getString("html_url");
                        String created = todoJSON.getString("created_at");
                        Todo todo = new Todo(title, body, url, created);
                        todoList.add(todo);
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return todoList;
    }
}
