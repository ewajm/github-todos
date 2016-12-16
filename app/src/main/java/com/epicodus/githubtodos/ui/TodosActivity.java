package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosActivity extends BaseActivity {
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.websiteUrlView) TextView mWebsiteUrlView;
    private Repo mRepo;

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

        TodosFragment fragment = new TodosFragment();
        Bundle args = new Bundle();
        args.putParcelable("repo", Parcels.wrap(mRepo));
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.todoFrameLayout, fragment).commit();
    }

}
