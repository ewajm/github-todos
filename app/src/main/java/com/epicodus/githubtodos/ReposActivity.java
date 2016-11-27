package com.epicodus.githubtodos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReposActivity extends AppCompatActivity {
    @Bind(R.id.greetingTextView)
    TextView mGreetingTextView;
    @Bind(R.id.projectListView)
    ListView mProjectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        mGreetingTextView.setText(String.format(getString(R.string.user_greeting), username));
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mGreetingTextView.setTypeface(sciFont);

        String[] sampleProjects = {"Sample Project 1", "Sample Project 2", "Sample Project 3", "To Do List"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sampleProjects);
        mProjectListView.setAdapter(adapter);
        mProjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent projectIntent = new Intent(ReposActivity.this, TodosActivity.class);
                projectIntent.putExtra("project", i);
                startActivity(projectIntent);
            }
        });
    }
}
