package com.epicodus.githubtodos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.lookupButton) Button mLookupButton;
    @Bind(R.id.usernameInput) EditText mUsernameInput;
    @Bind(R.id.usernameLoginInput) EditText mUsernameLoginInput;
    @Bind(R.id.passwordInput) EditText mPasswordInput;
    @Bind (R.id.subHeadingView) TextView mSubHeadingView;
    @Bind (R.id.beginTextView) TextView mBeginTextView;
    @Bind(R.id.asideTextView) TextView mAsideTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
            String username = mUsernameInput.getText().toString();
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
}
