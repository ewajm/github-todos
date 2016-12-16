package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.ui.TodosActivity;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 12/16/16.
 */
public class SavedRepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.repoNameTextView) TextView mRepoNameTextView;
    @Bind(R.id.repoLanguageTextView) TextView mRepoLanguageTextView;
    @Bind(R.id.repoDescriptionTextView) TextView mRepoDescriptionTextView;
    @Bind(R.id.reorderIconView) ImageView mReorderIconView;
    Repo mRepo;

    Context mContext;

    public SavedRepoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindRepo(Repo repo) {
        mRepo = repo;
        mRepoNameTextView.setText(repo.getName());
        mRepoLanguageTextView.setText(repo.getLanguage());
        mRepoDescriptionTextView.setText(repo.getDescription());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, TodosActivity.class);
        intent.putExtra("repo", Parcels.wrap(mRepo));
        mContext.startActivity(intent);
    }
}
