package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.adapters.RepoListAdapter;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.User;
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
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        getRepos(mUsername);
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
                ReposActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mRepos.size() > 0) {
                            mGreetingTextView.setText(String.format(getString(R.string.user_greeting), mUsername));
                            User.setUsername(mUsername);
                            mAdapter = new RepoListAdapter(mRepos, getApplicationContext());
                            mProjectRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReposActivity.this);
                            mProjectRecyclerView.setLayoutManager(layoutManager);
                            mProjectRecyclerView.setHasFixedSize(true);
                        }  else {
                            Toast.makeText(ReposActivity.this, "Sorry, it appears we couldn't find that username!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReposActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
