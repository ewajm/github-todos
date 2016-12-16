package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosActivity extends BaseActivity {
    public static final String TAG = TodosActivity.class.getSimpleName();
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.todoListView) ListView mTodoListView;
    @Bind(R.id.websiteUrlView) TextView mWebsiteUrlView;
    private ArrayList<Todo> mTodoArray = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTodoTitles;
    private Repo mRepo;
    private SharedPreferences mSharedPreferences;
    private String mCurrentUsername;
    private String mUserId;
    private Query mRepoQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        //member variable set up
        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));

        //cosmetic things
        mProjectNameView.setText(mRepo.getName());
        mWebsiteUrlView.setText(mRepo.getUrl());
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mProjectNameView.setTypeface(sciFont);

        mWebsiteUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRepo.getUrl()));
                startActivity(webIntent);
            }
        });

    }

}
