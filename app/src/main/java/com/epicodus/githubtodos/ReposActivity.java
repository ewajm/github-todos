package com.epicodus.githubtodos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReposActivity extends AppCompatActivity {
    @Bind(R.id.greetingTextView)
    TextView mGreetingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        mGreetingTextView.setText("Hello " + username);
    }
}
