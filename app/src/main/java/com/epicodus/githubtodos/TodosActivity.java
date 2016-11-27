package com.epicodus.githubtodos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosActivity extends AppCompatActivity {
    private static final String TAG = TodosActivity.class.getSimpleName();
    @Bind(R.id.projectNameView)
    TextView mProjectNameView;
    @Bind(R.id.todoListView)
    ListView mTodoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int position = intent.getIntExtra("project", 0);
        String projectName = intent.getStringExtra("projectName");
        mProjectNameView.setText(projectName);
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mProjectNameView.setTypeface(sciFont);
        String[][] todoArrays = {{"Make thing work", "Make it not look terrible", "Figure out how to do the thing"}, {"Make it stop doing the thing", "Figure out why its doing the thing", "Make the colors not eyesearing"}, {"Add feature", "Remove feature", "Think of more features"}, {"Refactor", "Rewrite", "Recycle"}};
        ArrayList<String> thisArray = new ArrayList<>(Arrays.asList(todoArrays[position]));
        Log.i(TAG, "onCreate: " + thisArray.toString());
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.custom_todo_list_item, thisArray);
        mTodoListView.setAdapter(adapter);

    }
}
