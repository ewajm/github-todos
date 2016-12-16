package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.ui.TodosActivity;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 12/16/16.
 */
public class SavedTodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.titleTextView) TextView mTitleTextView;
    @Bind(R.id.difficultyTextView) TextView mDifficultyTextView;
    Todo mTodo;

    Context mContext;

    public SavedTodoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindTodo(Todo todo) {
        mTodo = todo;
        mTitleTextView.setText(todo.getTitle());
        mDifficultyTextView.setText("Difficulty: " + mTodo.getDifficulty());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, TodosActivity.class);
        intent.putExtra("todo", Parcels.wrap(mTodo));
        mContext.startActivity(intent);
    }
}
