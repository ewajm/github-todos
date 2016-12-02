package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.services.GithubService;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReposActivity extends AppCompatActivity {
    private static final String TAG = ReposActivity.class.getSimpleName();
    @Bind(R.id.greetingTextView) TextView mGreetingTextView;
    @Bind(R.id.projectListView) ListView mProjectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        getRepos(username);
        mGreetingTextView.setText(String.format(getString(R.string.user_greeting), username));
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mGreetingTextView.setTypeface(sciFont);

        //this will be replaced by api/database lookup
        final String[] sampleProjects = {"Sample Project 1", "Sample Project 2", "Sample Project 3", "To Do List"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_list_item, sampleProjects);
        mProjectListView.setAdapter(adapter);
        mProjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent projectIntent = new Intent(ReposActivity.this, TodosActivity.class);
                projectIntent.putExtra("project", i);
                projectIntent.putExtra("projectName", sampleProjects[i]);
                startActivity(projectIntent);
            }
        });
    }

    public void getRepos(String username){
        final GithubService githubService = new GithubService();
        githubService.getUserRepos(username, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.i(TAG, "onResponse: " + jsonData);
            }
        });
    }
}
