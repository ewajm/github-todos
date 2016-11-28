package com.epicodus.githubtodos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.todoListView) ListView mTodoListView;
    @Bind(R.id.addTodoInput) EditText mAddTodoInput;
    @Bind(R.id.addTodoButton) Button mAddTodoButton;
    private ArrayList<String> mThisTodoArray;
    private ArrayAdapter mAdapter;

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

        //this will be replaced by database/api lookup
        String[][] todoArrays = {{"Make thing work", "Make it not look terrible", "Figure out how to do the thing"}, {"Make it stop doing the thing", "Figure out why its doing the thing", "Make the colors not eyesearing"}, {"Add feature", "Remove feature", "Think of more features"}, {"Refactor", "Rewrite", "Recycle"}};
        mThisTodoArray = new ArrayList<>(Arrays.asList(todoArrays[position]));

        mAdapter = new ArrayAdapter(this, R.layout.custom_todo_list_item, mThisTodoArray);
        mTodoListView.setAdapter(mAdapter);
        mAddTodoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String newTodo = mAddTodoInput.getText().toString();
        if(newTodo.length() > 0){
            mThisTodoArray.add(newTodo);
            mAdapter.notifyDataSetChanged();

            //for hiding the keyboard after button is pressed
            InputMethodManager imm = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mAddTodoInput.getWindowToken(), 0);
        } else {
            Toast.makeText(TodosActivity.this, "Please enter a todo first!", Toast.LENGTH_LONG).show();
        }
    }
}
