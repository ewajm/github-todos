package com.epicodus.githubtodos.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoDetailFragment extends Fragment {
    private static final String TAG = "TodoDetailFragment";
    @Bind(R.id.todoTitleTextView) TextView mTodoTitleTextView;
    @Bind(R.id.createdTextView) TextView mCreatedTextView;
    @Bind(R.id.bodyTextView) TextView mBodyTextView;
    @Bind(R.id.websiteTextView) TextView mWebsiteTextView;
    @Bind(R.id.difficultyTextView) TextView mDifficultyTextView;
    @Bind(R.id.urgencyTextView) TextView mUrgencyTextView;
    @Bind(R.id.addNoteButton) TextView mAddNoteButton;
    @Bind(R.id.addNoteEditText) TextView mAddNoteEditText;
    @Bind(R.id.addNoteLinearLayout) LinearLayout mAddNoteLinearLayout;
    @Bind(R.id.notesTextView) TextView mNotesTextView;
    @Bind(R.id.notesTitleTextView) TextView mNotesTitleTextView;

    private Todo mTodo;
    private Repo mRepo;
    private boolean mGithub;
    private String mUserId;
    private SharedPreferences mSharedPreferences;
    private DatabaseReference mTodoRef;

    public static TodoDetailFragment newInstance(Todo todo, Repo repo) {
        TodoDetailFragment todoDetailFragment = new TodoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("todo", Parcels.wrap(todo));
        args.putParcelable("repo", Parcels.wrap(repo));
        todoDetailFragment.setArguments(args);
        return todoDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mTodo = Parcels.unwrap(getArguments().getParcelable("todo"));
        mRepo = Parcels.unwrap(getArguments().getParcelable("repo"));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mGithub = mSharedPreferences.getBoolean(Constants.PREFERENCES_GITHUB_TOGGLE_KEY, false);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_todo_detail, container, false);
        ButterKnife.bind(this, view);
        mTodoTitleTextView.setText(mTodo.getTitle());
        if(mTodo.getBody().length()>0){
            mBodyTextView.setText(mTodo.getBody());
            mBodyTextView.setVisibility(View.VISIBLE);
        }
        mCreatedTextView.setText(mTodo.getCreated());
        if(mTodo.getUrl() != null){
            mWebsiteTextView.setText(mTodo.getUrl());
            mWebsiteTextView.setVisibility(View.VISIBLE);
        } else {
            mWebsiteTextView.setVisibility(View.GONE);
        }
        if(mTodo.getNotes()!= null){
            mNotesTextView.setText(mTodo.getNotes());
        }
        if(mTodo.getUrgency() > 0){
            mUrgencyTextView.setVisibility(View.VISIBLE);
            mUrgencyTextView.setText("Urgency: " + mTodo.getUrgency());
        }
        if(mTodo.getDifficulty() > 0){
            mDifficultyTextView.setVisibility(View.VISIBLE);
            mDifficultyTextView.setText("Difficulty: " + mTodo.getDifficulty());
        }

        mTodoRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId);
        if(mGithub){
            checkFirebaseForTodo();
        } else {
            mNotesTitleTextView.setVisibility(View.VISIBLE);
            mNotesTextView.setVisibility(View.VISIBLE);
            mAddNoteLinearLayout.setVisibility(View.VISIBLE);
            mAddNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String note = mAddNoteEditText.getText().toString().trim();
                    if(note.length()>0){
                        if(mTodo.getNotes() != null){
                            mTodo.setNotes(mTodo.getNotes() + "\n" + note);
                        } else {
                            mTodo.setNotes(note);
                        }
                        mNotesTextView.setText(mTodo.getNotes());
                        mTodoRef.child(mTodo.getPushId()).child("notes").setValue(mTodo.getNotes());
                        //for hiding the keyboard after button is pressed
                        InputMethodManager imm = (InputMethodManager)getActivity().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mAddNoteEditText.getWindowToken(), 0);
                        mAddNoteEditText.setText("");

                    } else {
                        Toast.makeText(getActivity(), "Write a note, first!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return view;
    }

    private void checkFirebaseForTodo() {
        Query repoQuery = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).orderByChild("url").equalTo(mTodo.getUrl());
        repoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        mTodo.setPushId(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: " + mGithub);
        if(mGithub){
            inflater.inflate(R.menu.menu_save_todo, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save_todo){
            if(mTodo.getPushId() == null){
                DatabaseReference repoRef =  FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId);
                DatabaseReference pushRef = mTodoRef.push();
                if(mRepo.getPushId() == null){
                    DatabaseReference repoPushRef = repoRef.push();
                    String repoPushId = repoPushRef.getKey();
                    mRepo.setPushId(repoPushId);
                    repoPushRef.setValue(mRepo);
                }
                mTodo.setRepoId(mRepo.getPushId());
                String pushId = pushRef.getKey();
                mTodo.setPushId(pushId);
                pushRef.setValue(mTodo);
                repoRef.child(mRepo.getPushId()).child("todos").child(pushId).setValue(true);
                Toast.makeText(getContext(), "Todo saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "You have already saved this Todo", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
