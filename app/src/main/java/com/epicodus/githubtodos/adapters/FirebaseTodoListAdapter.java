package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.ui.TodoDetailActivity;
import com.epicodus.githubtodos.utils.OnStartDragListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Guest on 12/16/16.
 */
public class FirebaseTodoListAdapter extends FirebaseRecyclerAdapter<Todo, SavedTodoViewHolder> implements ItemTouchHelperAdapter{
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Todo> mTodos = new ArrayList<>();
    private Repo mRepo;

    public FirebaseTodoListAdapter(Class<Todo> modelClass, int modelLayout,
                           Class<SavedTodoViewHolder> viewHolderClass,
                           Query ref, OnStartDragListener onStartDragListener, Context context, Repo repo) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mRepo = repo;
        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mTodos.add(dataSnapshot.getValue(Todo.class));
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


    @Override
    protected void populateViewHolder(final SavedTodoViewHolder viewHolder, Todo model, int position) {
        viewHolder.bindTodo(model);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TodoDetailActivity.class);
                intent.putExtra("position", viewHolder.getAdapterPosition());
                intent.putExtra("todos", Parcels.wrap(mTodos));
                intent.putExtra("repo", Parcels.wrap(mRepo));
                mContext.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Todo todo = mTodos.get(viewHolder.getAdapterPosition());
                if(!todo.isToDone()) {
                    viewHolder.mUrgencyImageView.setImageResource(R.drawable.ic_action_done);
                    viewHolder.mUrgencyImageView.animate().rotationYBy(360).setDuration(1000);
                    viewHolder.mTodoItemLayout.setBackgroundColor(Color.parseColor("#607D8B"));
                    todo.setToDone(true);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mTodos.remove(position);
        getRef(position).removeValue();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mRef.removeEventListener(mChildEventListener);
    }

}
