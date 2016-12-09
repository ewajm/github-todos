package com.epicodus.githubtodos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;

import java.util.ArrayList;

/**
 * Created by Ewa on 12/2/2016.
 */
public class RepoListAdapter extends RecyclerView.Adapter<RepoViewHolder> {
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

}
