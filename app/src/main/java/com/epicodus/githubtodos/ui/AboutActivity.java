package com.epicodus.githubtodos.ui;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.epicodus.githubtodos.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.getStartedView) TextView mGetStartedView;
    @Bind(R.id.subHeadingView) TextView mSubHeadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mSubHeadingView.setTypeface(sciFont);
        mGetStartedView.setTypeface(sciFont);
    }
}
