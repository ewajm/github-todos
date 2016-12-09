package com.epicodus.githubtodos.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.adapters.TodoPagerAdapter;
import com.epicodus.githubtodos.models.Todo;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager) ViewPager mViewPager;
    private TodoPagerAdapter mTodoPagerAdapter;
    ArrayList<Todo> mTodos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        ButterKnife.bind(this);

        mTodos = Parcels.unwrap(getIntent().getParcelableExtra("todos"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        mTodoPagerAdapter = new TodoPagerAdapter(getSupportFragmentManager(), mTodos);
        mViewPager.setAdapter(mTodoPagerAdapter);
        mViewPager.setCurrentItem(startingPosition);
    }
}
