package com.epicodus.githubtodos.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class TodoDetailFragment extends Fragment {
    private static final String TAG = "TodoDetailFragment";
    @Bind(R.id.todoTitleTextView) TextView mTodoTitleTextView;
    @Bind(R.id.createdTextView) TextView mCreatedTextView;
    @Bind(R.id.bodyTextView) TextView mBodyTextView;
    @Bind(R.id.websiteTextView) TextView mWebsiteTextView;

    private Todo mTodo;
    private Repo mRepo;
    private boolean mGithub;
    private String mUserId;

    public static TodoDetailFragment newInstance(Todo todo, Repo repo, boolean github) {
        TodoDetailFragment todoDetailFragment = new TodoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("todo", Parcels.wrap(todo));
        args.putParcelable("repo", Parcels.wrap(repo));
        args.putBoolean("github", github);
        todoDetailFragment.setArguments(args);
        return todoDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mTodo = Parcels.unwrap(getArguments().getParcelable("todo"));
        mRepo = Parcels.unwrap(getArguments().getParcelable("repo"));
        mGithub = getArguments().getBoolean("github");
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_todo_detail, container, false);
        ButterKnife.bind(this, view);
        mTodoTitleTextView.setText(mTodo.getTitle());
        mBodyTextView.setText(mTodo.getBody());
        mCreatedTextView.setText(mTodo.getCreated());
        mWebsiteTextView.setText(mTodo.getUrl());
        checkFirebaseForTodo();

        return view;
    }

    private void checkFirebaseForTodo() {
        Query repoQuery = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).orderByChild("url").equalTo(mTodo.getUrl());
        repoQuery.addValueEventListener(new ValueEventListener() {
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
            inflater.inflate(R.menu.menu_save, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            if(mTodo.getPushId() == null){
                DatabaseReference todoRef =  FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId);
                DatabaseReference repoRef =  FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId);
                DatabaseReference pushRef = todoRef.push();
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
