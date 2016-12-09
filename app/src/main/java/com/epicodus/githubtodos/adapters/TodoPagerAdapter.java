package com.epicodus.githubtodos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.ui.TodoDetailFragment;

import java.util.ArrayList;

/**
 * Created by Ewa on 12/2/2016.
 */
public class TodoPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Todo> mTodos;
    Repo mRepo;
    boolean mGithub;

    public TodoPagerAdapter(FragmentManager fm, ArrayList<Todo> todos, Repo repo, boolean github) {
        super(fm);
        mTodos = todos;
        mRepo= repo;
        mGithub=github;
    }

    @Override
    public Fragment getItem(int position) {
        return TodoDetailFragment.newInstance(mTodos.get(position), mRepo, mGithub);
    }

    @Override
    public int getCount() {
        return mTodos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTodos.get(position).getCreated();
    }
}
