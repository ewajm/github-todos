package com.epicodus.githubtodos.adapters;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.ui.TodoDetailActivity;
import com.epicodus.githubtodos.ui.TodoDetailFragment;
import com.epicodus.githubtodos.utils.OnStartDragListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Guest on 12/16/16.
 */
public class FirebaseTodoListAdapter extends FirebaseRecyclerAdapter<Todo, SavedTodoViewHolder> implements ItemTouchHelperAdapter{
    private Query mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Todo> mTodos;
    private Repo mRepo;
    private int mOrientation;

    public FirebaseTodoListAdapter(Class<Todo> modelClass, int modelLayout,
                           Class<SavedTodoViewHolder> viewHolderClass,
                           Query ref, OnStartDragListener onStartDragListener, Context context, Repo repo) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref;
        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mRepo = repo;
        mTodos = new ArrayList<>();

        mChildEventListener = ref.addChildEventListener(new ChildEventListener() {
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

        mOrientation = viewHolder.itemView.getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            createDetailFragment(0);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                    createDetailFragment(position);
                } else {
                    Intent intent = new Intent(mContext, TodoDetailActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("todos", Parcels.wrap(mTodos));
                    intent.putExtra("repo", Parcels.wrap(mRepo));
                    mContext.startActivity(intent);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Todo todo = mTodos.get(viewHolder.getAdapterPosition());
                if(!todo.isToDone()) {
                    viewHolder.mUrgencyImageView.setImageResource(R.drawable.ic_action_done);
                    ViewPropertyAnimator viewPropertyAnimator = viewHolder.mUrgencyImageView.animate().rotationYBy(360).setDuration(1000);
                    viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewHolder.mTodoItemLayout.setBackgroundColor(Color.parseColor("#607D8B"));
                            todo.setToDone(true);
                            getRef(viewHolder.getAdapterPosition()).child(Constants.FIREBASE_TODONE_REFERENCE).setValue(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                return true;
            }
        });
    }

    private void createDetailFragment(int position) {
        // Creates new RestaurantDetailFragment with the given position:
        TodoDetailFragment detailFragment = TodoDetailFragment.newInstance(mTodos.get(position), mRepo);
        // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        //  Replaces the FrameLayout with the RestaurantDetailFragment:
        ft.replace(R.id.todoDetailFrameLayout, detailFragment);
        // Commits these changes:
        ft.commit();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(final int position) {
        new AlertDialog.Builder(mContext)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete")
                .setMessage("Delete TODO forever?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTodos.remove(position);
                        getRef(position).removeValue();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mRef.removeEventListener(mChildEventListener);
    }

}
