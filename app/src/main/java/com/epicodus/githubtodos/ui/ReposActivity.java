package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.Constants;
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
//TODO: probs want to refactor this into two fragments

public class ReposActivity extends BaseActivity {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentUsernameSearch;
    private static final String TAG = ReposActivity.class.getSimpleName();
    @Bind(R.id.greetingTextView) TextView mGreetingTextView;
    @Bind(R.id.projectRecyclerView) RecyclerView mProjectRecyclerView;
    @Bind(R.id.emptyView) TextView mEmptyView;
    public ArrayList<Repo> mRepos;
    private RepoListAdapter mAdapter;
    private int mOrientation;
    private boolean mGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mGithub = mSharedPreferences.getBoolean(Constants.PREFERENCES_GITHUB_TOGGLE_KEY, false);
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mGreetingTextView.setTypeface(sciFont);
        mOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentUsernameSearch = mSharedPreferences.getString(Constants.PREFERENCES_USERNAME_KEY, null);
        if(mRecentUsernameSearch != null){
            getRepos(mRecentUsernameSearch);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mProjectRecyclerView.setVisibility(View.GONE);
        }
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
                            if(mProjectRecyclerView.getVisibility() == View.GONE){
                                mProjectRecyclerView.setVisibility(View.VISIBLE);
                                mEmptyView.setVisibility(View.GONE);
                            }
                            mGreetingTextView.setText(String.format(getString(R.string.github_repos_heading), mRecentUsernameSearch));
                            mAdapter = new RepoListAdapter(mRepos, getApplicationContext());
                            mProjectRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager;
                            if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                                layoutManager = new GridLayoutManager(ReposActivity.this, 2);
                            } else {
                                layoutManager = new LinearLayoutManager(ReposActivity.this);
                            }
                            mProjectRecyclerView.setLayoutManager(layoutManager);
                            mProjectRecyclerView.setHasFixedSize(true);
                        }  else {
                            if(mProjectRecyclerView.getVisibility() == View.VISIBLE){
                                mProjectRecyclerView.setVisibility(View.GONE);
                                mEmptyView.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(ReposActivity.this, "Sorry, it appears we couldn't find that username!", Toast.LENGTH_SHORT).show();
                            mGreetingTextView.setText("");
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query.trim());
                getRepos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void addToSharedPreferences(String username) {
        mEditor.putString(Constants.PREFERENCES_USERNAME_KEY, username).apply();
        mRecentUsernameSearch = username;
    }

}
