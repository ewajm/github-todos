package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

//TODO: convert token usage to Oauth: Github API allows a Basic Authorization call (username + password) to the authorizations path in order to generate OAuth fields
//TODO: authentications from this call can then be saved to the static user class and used throughout app
//TODO: github login as overflow menu bit -> dialog?!?!?!

public class MainActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.firebaseLookupButton) Button mFirebaseButton;
    @Bind(R.id.githubLookupButton) Button mGithubButton;
    @Bind (R.id.subHeadingView) TextView mSubHeadingView;
    @Bind (R.id.beginTextView) TextView mBeginTextView;
    @Bind(R.id.asideTextView) TextView mAsideTextView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    getSupportActionBar().setTitle("Welcome, " + user.getDisplayName() + "!");
                } else {

                }
            }
        };
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mSubHeadingView.setTypeface(sciFont);
        mBeginTextView.setTypeface(sciFont);
        mAsideTextView.setTypeface(sciFont);
        mFirebaseButton.setOnClickListener(this);
        mGithubButton.setOnClickListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public void onClick(View view) {
        boolean github = view.getId() == R.id.githubLookupButton;
        addToSharedPreferences(github);
        if(github){
            Intent intent = new Intent(MainActivity.this, ReposActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, SavedReposActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void addToSharedPreferences(boolean github) {
        mEditor.putBoolean(Constants.PREFERENCES_GITHUB_TOGGLE_KEY, github).apply();
    }
}
