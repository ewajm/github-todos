package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.adapters.FirebaseTodoListAdapter;
import com.epicodus.githubtodos.adapters.OnStartDragListener;
import com.epicodus.githubtodos.adapters.SavedTodoViewHolder;
import com.epicodus.githubtodos.adapters.SimpleItemTouchHelperCallback;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedTodosActivity extends BaseActivity implements OnStartDragListener {
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.todoRecyclerView) RecyclerView mTodoListView;
    @Bind(R.id.websiteUrlView) TextView mWebsiteUrlView;
    @Bind(R.id.emptyView) TextView mEmptyView;
    private ArrayList<Todo> mTodoArray = new ArrayList<>();
    private Repo mRepo;
    private String mUserId;
    private Query mRepoQuery;
    private FirebaseTodoListAdapter mFirebaseAdapter;
    private Query mTodoRef;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_todos);
        ButterKnife.bind(this);

        //member variable set up
        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //cosmetic things
        mProjectNameView.setText(mRepo.getName());
        mWebsiteUrlView.setText(mRepo.getUrl());
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mProjectNameView.setTypeface(sciFont);

        mWebsiteUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRepo.getUrl()));
                startActivity(webIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mTodoRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).orderByChild("repoId").equalTo(mRepo.getPushId());
        mFirebaseAdapter = new FirebaseTodoListAdapter(Todo.class, R.layout.saved_todo_list_item, SavedTodoViewHolder.class, mTodoRef, this, this);
        mTodoListView.setHasFixedSize(true);
        mTodoListView.setLayoutManager(new LinearLayoutManager(this));
        mTodoListView.setAdapter(mFirebaseAdapter);
        //mTodoListView.setOnItemClickListener(todoListItemClickListener());

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mTodoListView);
    }

    private void checkFirebaseForRepo() {
        mRepoQuery = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId).orderByChild("url").equalTo(mRepo.getUrl());
        mRepoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mRepo.setPushId(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


//    @NonNull
//    private AdapterView.OnItemClickListener todoListItemClickListener() {
//        return new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//                mTodoRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                            mTodoArray.add(snapshot.getValue(Todo.class));
//                        }
//                        //intent must be explicitly called within on data change or will fire too early
//                        Intent intent = new Intent(TodosActivity.this, TodoDetailActivity.class);
//                        intent.putExtra("todos", Parcels.wrap(mTodoArray));
//                        intent.putExtra("repo", Parcels.wrap(mRepo));
//                        intent.putExtra("position", i);
//                        startActivity(intent);
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        };
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.cleanup();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(this, AddTodoActivity.class);
            intent.putExtra("repo", Parcels.wrap(mRepo));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
