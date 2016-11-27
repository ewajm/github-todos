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

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.loginButton)
    Button mLoginButton;
    @Bind(R.id.usernameInput)
    EditText mUsernameInput;
    @Bind (R.id.subHeadingView)
    TextView mSubHeadingView;
    @Bind (R.id.beginTextView) TextView mBeginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mSubHeadingView.setTypeface(sciFont);
        mBeginTextView.setTypeface(sciFont);
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
