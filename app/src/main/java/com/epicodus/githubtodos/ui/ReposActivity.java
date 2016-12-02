package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.adapters.RepoListAdapter;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.services.GithubService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//TODO: add username validation

public class ReposActivity extends AppCompatActivity {
    private static final String TAG = ReposActivity.class.getSimpleName();
    @Bind(R.id.greetingTextView) TextView mGreetingTextView;
    @Bind(R.id.projectRecyclerView) RecyclerView mProjectRecyclerView;
    public ArrayList<Repo> mRepos;
    private RepoListAdapter mAdapter;

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
                mRepos = githubService.processRepoResponse(response);
                //this will be replaced by api/database lookup
                ReposActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new RepoListAdapter(mRepos, getApplicationContext());
                        mProjectRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReposActivity.this);
                        mProjectRecyclerView.setLayoutManager(layoutManager);
                    }
                });

//                mProjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Intent projectIntent = new Intent(ReposActivity.this, TodosActivity.class);
//                        projectIntent.putExtra("project", i);
//                        projectIntent.putExtra("projectName", projectNames[i]);
//                        startActivity(projectIntent);
//                    }
//                });
            }
        });
    }
}
