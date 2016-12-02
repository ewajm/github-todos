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
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTodoTitles;
    private Repo mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mProjectNameView.setText(mRepo.getName());
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mProjectNameView.setTypeface(sciFont);
        getTodos(mRepo.getName());
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
                mTodoTitles = new ArrayList<>();
                for(int i = 0; i < mTodoArray.size(); i++){
                    mTodoTitles.add(mTodoArray.get(i).getTitle());
                }
                TodosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ArrayAdapter<>(TodosActivity.this, R.layout.custom_todo_list_item, mTodoTitles);
                        mTodoListView.setAdapter(mAdapter);
                        mAddTodoButton.setOnClickListener(TodosActivity.this);
                    }
                });
            }
        });
    }



    @Override
    public void onClick(View view) {
        if(mAddTodoInput.getText().toString().length() > 0){
            Todo newTodo = new Todo();
            newTodo.setTitle("TODO: " + mAddTodoInput.getText().toString());
            mTodoArray.add(newTodo);
            mTodoTitles.add(newTodo.getTitle());
            mAdapter.notifyDataSetChanged();

            //for hiding the keyboard after button is pressed
            InputMethodManager imm = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mAddTodoInput.getWindowToken(), 0);
        } else {
            Toast.makeText(TodosActivity.this, "Please enter a todo first!", Toast.LENGTH_LONG).show();
        }
    }
}
