package com.epicodus.githubtodos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.loginButton)
    Button mLoginButton;
    @Bind(R.id.usernameInput)
    EditText mUsernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsernameInput.getText().toString();
                if(username.length() > 0){
                    Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a username!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
