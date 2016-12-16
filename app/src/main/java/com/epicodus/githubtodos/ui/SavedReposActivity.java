package com.epicodus.githubtodos.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.adapters.FirebaseRepoListAdapter;
import com.epicodus.githubtodos.adapters.OnStartDragListener;
import com.epicodus.githubtodos.adapters.SavedRepoViewHolder;
import com.epicodus.githubtodos.adapters.SimpleItemTouchHelperCallback;
import com.epicodus.githubtodos.models.Repo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedReposActivity extends BaseActivity implements OnStartDragListener {
    private static final String TAG = SavedReposActivity.class.getSimpleName();
    @Bind(R.id.greetingTextView) TextView mGreetingTextView;
    @Bind(R.id.projectRecyclerView) RecyclerView mProjectRecyclerView;
    @Bind(R.id.emptyView) TextView mEmptyView;
    public ArrayList<Repo> mRepos;
    private Query mRepoReference;
    private FirebaseRepoListAdapter mFirebaseAdapter;
    private ValueEventListener mRepoValueEventListener;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);

        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mGreetingTextView.setTypeface(sciFont);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepoReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(userId).orderByChild(Constants.FIREBASE_QUERY_INDEX);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRepoListAdapter(Repo.class, R.layout.repo_list_item_drag, SavedRepoViewHolder.class, mRepoReference, this, this);

        mRepoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    if(mProjectRecyclerView.getVisibility() == View.VISIBLE){
                        mProjectRecyclerView.setVisibility(View.GONE);
                        mEmptyView.setText(R.string.no_saved_repos);
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(mProjectRecyclerView.getVisibility() == View.GONE){
                        mProjectRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProjectRecyclerView.setHasFixedSize(true);
        mProjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProjectRecyclerView.setAdapter(mFirebaseAdapter);
        mGreetingTextView.setText(getString(R.string.firebase_repos_heading));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mProjectRecyclerView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.cleanup();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
