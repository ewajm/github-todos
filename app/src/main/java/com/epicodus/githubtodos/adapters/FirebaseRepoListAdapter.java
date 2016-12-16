package com.epicodus.githubtodos.adapters;

import android.content.Context;

import com.epicodus.githubtodos.models.Repo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Guest on 12/16/16.
 */
public class FirebaseRepoListAdapter extends FirebaseRecyclerAdapter<Repo, SavedRepoViewHolder> implements ItemTouchHelperAdapter{
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;

    public FirebaseRepoListAdapter(Class<Repo> modelClass, int modelLayout,
                                   Class<SavedRepoViewHolder> viewHolderClass,
                                   Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
    }

    @Override
    protected void populateViewHolder(SavedRepoViewHolder viewHolder, Repo model, int position) {
        viewHolder.bindRepo(model);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

}
