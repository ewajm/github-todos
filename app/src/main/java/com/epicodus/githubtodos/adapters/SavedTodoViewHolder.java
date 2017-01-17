package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Todo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 12/16/16.
 */
public class SavedTodoViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.titleTextView) TextView mTitleTextView;
    @Bind(R.id.urgencyImageView) ImageView mUrgencyImageView;
    @Bind(R.id.todoItemLayout) RelativeLayout mTodoItemLayout;
    @Bind(R.id.todoTypeIcon) ImageView mTodoTypeIcon;
    Todo mTodo;
    String[] mColors = {"#37593D", "#756048", "#754B48", "#607D8B"};

    Context mContext;

    public SavedTodoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void bindTodo(Todo todo) {
        mTodo = todo;
        mTitleTextView.setText(todo.getTitle());
        int[] typeIcons = {R.drawable.ic_feature, R.drawable.ic_tweak, R.drawable.ic_bug};
        mTodoTypeIcon.setImageResource(typeIcons[mTodo.getType()]);
        if(mTodo.isToDone()){
            mUrgencyImageView.setImageResource(R.drawable.ic_action_done);
            mTodoItemLayout.setBackgroundColor(Color.parseColor(mColors[3]));
        } else {
            switch (mTodo.getUrgency()) {
                case 1:
                case 2:
                    mUrgencyImageView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    mUrgencyImageView.setImageResource(R.drawable.ic_least_urgent);
                    break;
                case 4:
                    mUrgencyImageView.setImageResource(R.drawable.ic_med_urgent);
                    break;
                case 5:
                    mUrgencyImageView.setImageResource(R.drawable.ic_most_urgent);
                    break;
            }
            int colorIndex = (int) Math.floor(mTodo.getDifficulty() / 2);
            mTodoItemLayout.setBackgroundColor(Color.parseColor(mColors[colorIndex]));
        }
    }

}
