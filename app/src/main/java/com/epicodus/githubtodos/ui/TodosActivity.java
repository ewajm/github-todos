package com.epicodus.githubtodos.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.services.GithubService;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TodosActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = TodosActivity.class.getSimpleName();
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.todoListView) ListView mTodoListView;
    @Bind(R.id.addTodoInput) EditText mAddTodoInput;
    @Bind(R.id.addTodoButton) Button mAddTodoButton;
    private ArrayList<Todo> mTodoArray;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Repo repo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mProjectNameView.setText(repo.getName());
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mProjectNameView.setTypeface(sciFont);
        getTodos(repo.getName());
        //this will be replaced by database/api lookup
//        String[][] todoArrays = {{"Make thing work", "Make it not look terrible", "Figure out how to do the thing"}, {"Make it stop doing the thing", "Figure out why its doing the thing", "Make the colors not eyesearing"}, {"Add feature", "Remove feature", "Think of more features"}, {"Refactor", "Rewrite", "Recycle"}};
//        mThisTodoArray = new ArrayList<>(Arrays.asList(todoArrays[position]));
//
//        mAdapter = new ArrayAdapter(this, R.layout.custom_todo_list_item, mThisTodoArray);
//        mTodoListView.setAdapter(mAdapter);
//        mAddTodoButton.setOnClickListener(this);
    }

    public void getTodos(String repoName){
        final GithubService githubService = new GithubService();
        githubService.getRepoIssues(repoName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mTodoArray = githubService.processIssueResponse(response);

            }
        });
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
