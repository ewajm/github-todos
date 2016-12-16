package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.epicodus.githubtodos.models.Repo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Guest on 12/16/16.
 */
public class FirebaseRepoListAdapter extends FirebaseRecyclerAdapter<Repo, SavedRepoViewHolder> implements ItemTouchHelperAdapter{
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Repo> mRepos = new ArrayList<>();

    public FirebaseRepoListAdapter(Class<Repo> modelClass, int modelLayout,
                                   Class<SavedRepoViewHolder> viewHolderClass,
                                   Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mRepos.add(dataSnapshot.getValue(Repo.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setIndexInFirebase() {
        for (Repo repo : mRepos) {
            int index = mRepos.indexOf(repo);
            DatabaseReference ref = getRef(index);
            repo.setIndex(Integer.toString(index));
            ref.setValue(repo);
        }
    }

    @Override
    protected void populateViewHolder(final SavedRepoViewHolder viewHolder, Repo model, int position) {
        viewHolder.bindRepo(model);
        viewHolder.mReorderIconView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mRepos, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mRepos.remove(position);
        getRef(position).removeValue();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        setIndexInFirebase();
        mRef.removeEventListener(mChildEventListener);
    }

}
