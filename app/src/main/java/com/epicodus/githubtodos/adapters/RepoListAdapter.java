package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.ui.TodosActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ewa on 12/2/2016.
 */
public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {
    private ArrayList<Repo> mRepos = new ArrayList<>();
    private Context mContext;

    public RepoListAdapter(ArrayList<Repo> repos, Context context) {
        mRepos = repos;
        mContext = context;
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_item, parent, false);
        RepoViewHolder viewHolder= new RepoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.bindRepo(mRepos.get(position));
    }

    @Override
    public int getItemCount() {
        return mRepos.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.repoNameTextView)
        TextView mRepoNameTextView;
        @Bind(R.id.repoLanguageTextView) TextView mRepoLanguageTextView;
        @Bind(R.id.repoDescriptionTextView) TextView mRepoDescriptionTextView;

        Context mContext;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindRepo(Repo repo){
            mRepoNameTextView.setText(repo.getName());
            mRepoLanguageTextView.setText(repo.getLanguage());
            mRepoDescriptionTextView.setText(repo.getDescription());
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Intent intent = new Intent(mContext, TodosActivity.class);
            intent.putExtra("repo", Parcels.wrap(mRepos.get(position)));
            mContext.startActivity(intent);
        }
    }
}
