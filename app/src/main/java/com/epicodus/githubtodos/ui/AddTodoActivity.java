package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.utils.DatabaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddTodoActivity extends AppCompatActivity {
    @Bind(R.id.titleEditText) EditText mTitleEditText;
    @Bind(R.id.bodyEditText) EditText mBodyEditText;
    @Bind(R.id.addTodoButton) Button mAddTodoButton;
    @Bind(R.id.urgencySeekBar) SeekBar mUrgencySeekBar;
    @Bind(R.id.difficultySeekBar) SeekBar mDifficultySeekBar;
    @Bind(R.id.todoTypeSpinner)
    Spinner mTodoTypeSpinner;

    private Todo mTodo;
    private Repo mRepo;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAddTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleEditText.getText().toString().trim();
                if(title.length() > 0 && !title.equals("TODO:")){
                    String body = mBodyEditText.getText().toString().trim();
                    int urgency = mUrgencySeekBar.getProgress();
                    int difficulty = mDifficultySeekBar.getProgress();
                    int type = mTodoTypeSpinner.getSelectedItemPosition();
                    mTodo = new Todo(title, body, urgency, difficulty, type);
                    DatabaseReference todoRef =  DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).child(mRepo.getPushId());
                    DatabaseReference repoRef =  DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId);
                    DatabaseReference pushRef = todoRef.push();
                    mTodo.setRepoId(mRepo.getPushId());
                    String pushId = pushRef.getKey();
                    mTodo.setPushId(pushId);
                    pushRef.setValue(mTodo);
                    repoRef.child(mRepo.getPushId()).child("todos").child(pushId).setValue(mTodo.getTitle());
                    Toast.makeText(AddTodoActivity.this, "Todo saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    mTitleEditText.setError("Please enter a title");
                }
            }
        });
    }
}
