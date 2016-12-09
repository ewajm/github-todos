package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

//TODO: create static user class
//TODO: convert token usage to Oauth: Github API allows a Basic Authorization call (username + password) to the authorizations path in order to generate OAuth fields
//TODO: authentications from this call can then be saved to the static user class and used throughout app
//TODO: turn this into a more of a dashboard type deal.
//TODO: user search = searchview
//TODO: github login as overflow menu bit -> dialog?!?!?!
//TODO: BaseActivity
//TODO: user -> shared prefs/github username


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.lookupButton) Button mLookupButton;
    @Bind(R.id.usernameInput) EditText mUsernameInput;
    @Bind(R.id.usernameLoginInput) EditText mUsernameLoginInput;
    @Bind(R.id.passwordInput) EditText mPasswordInput;
    @Bind (R.id.subHeadingView) TextView mSubHeadingView;
    @Bind (R.id.beginTextView) TextView mBeginTextView;
    @Bind(R.id.asideTextView) TextView mAsideTextView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
        mLoginButton.setOnClickListener(this);
        mLookupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mLookupButton){
            String username = mUsernameInput.getText().toString().trim();
            if(username.length() > 0){
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a username!", Toast.LENGTH_SHORT).show();
            }
        } else {
            String username = mUsernameLoginInput.getText().toString();
            String password = mPasswordInput.getText().toString();
            if(username.length() > 0 && password.length() > 0){
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a username and password!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
}
