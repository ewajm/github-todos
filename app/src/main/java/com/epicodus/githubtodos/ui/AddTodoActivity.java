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
import org.parceler.converter.HashMapParcelConverter;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


//TODO: reload todo after it is saved
//TODO: deal with null notes
//TODO: deal with saving notes?

public class AddTodoActivity extends AppCompatActivity {
    @Bind(R.id.titleEditText) EditText mTitleEditText;
    @Bind(R.id.bodyEditText) EditText mBodyEditText;
    @Bind(R.id.addTodoButton) Button mAddTodoButton;
    @Bind(R.id.cancelButton) Button mCancelButton;
    @Bind(R.id.urgencySeekBar) SeekBar mUrgencySeekBar;
    @Bind(R.id.difficultySeekBar) SeekBar mDifficultySeekBar;
    @Bind(R.id.notesEditText) EditText mNotesEditText;
    @Bind(R.id.todoTypeSpinner) Spinner mTodoTypeSpinner;

    private Todo mTodo;
    private Repo mRepo;
    private String mUserId;
    private boolean mEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mTodo = Parcels.unwrap(intent.getParcelableExtra("todo"));
        if(mTodo != null){
            mEditMode = true;
            mTitleEditText.setText(mTodo.getTitle());
            mBodyEditText.setText(mTodo.getBody());
            mDifficultySeekBar.setProgress(mTodo.getDifficulty());
            mUrgencySeekBar.setProgress(mTodo.getUrgency());
            mTodoTypeSpinner.setSelection(mTodo.getType());
            mNotesEditText.setVisibility(View.VISIBLE);
            String noteString = mTodo.getNotes()+"/n";
            mNotesEditText.setText(noteString);
        }
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
                    if(mEditMode){
                        String notes = mNotesEditText.getText().toString().trim();
                        HashMap<String, Object> updateMap = new HashMap<>();
                        if(!mTodo.getTitle().equals(title)){
                            updateMap.put("title", title);
                        }
                        if(!mTodo.getBody().equals(body)){
                            updateMap.put("body", body.length() == 0 ? null: body);
                        }
                        if(mTodo.getDifficulty() != difficulty){
                            updateMap.put("difficulty", difficulty == 0 ? null: difficulty);
                        }
                        if(mTodo.getUrgency() != urgency){
                            updateMap.put("urgency", urgency == 0 ? null: urgency);
                        }
                        if(mTodo.getType() != type){
                            updateMap.put("type", type);
                        }
                        if(mTodo.getNotes() == null || !mTodo.getNotes().equals(notes)){
                            updateMap.put("notes", notes.length() == 0 ? null: notes);
                        }
                        if(updateMap.size() > 0){
                            DatabaseReference todoRef = DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).child(mTodo.getRepoId()).child(mTodo.getPushId());
                            todoRef.updateChildren(updateMap);
                        }
                    } else {
                        mTodo = new Todo(title, body, urgency, difficulty, type);
                        DatabaseReference todoRef =  DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).child(mRepo.getPushId());
                        DatabaseReference repoRef =  DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId);
                        DatabaseReference pushRef = todoRef.push();
                        mTodo.setRepoId(mRepo.getPushId());
                        String pushId = pushRef.getKey();
                        mTodo.setPushId(pushId);
                        pushRef.setValue(mTodo);
                        repoRef.child(mRepo.getPushId()).child("todos").child(pushId).setValue(mTodo.getTitle());
                    }
                    Toast.makeText(AddTodoActivity.this, "Todo saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    mTitleEditText.setError("Please enter a title");
                }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String createNotesString() {
        return "";
    }
}
