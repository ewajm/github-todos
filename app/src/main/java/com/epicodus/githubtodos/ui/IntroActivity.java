package com.epicodus.githubtodos.ui;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epicodus.githubtodos.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("What can you do with Github TODOs?", "Look up your Github Repos (or other Repos you are interested in)", R.drawable.reposearch, Color.parseColor("#173B4D")));
        addSlide(AppIntroFragment.newInstance("View and save a Repo's TODOs", "Github TODOs currently looks through a repo's issues to find potential TODOs", R.drawable.repoview1, Color.parseColor("#2B4E5F")));
        addSlide(AppIntroFragment.newInstance("View saved Repos and TODOs", "Mark TODOs as finished by pressing and holding, delete them by swiping, or add your notes by going to a TODO's detail page", R.drawable.repoview2, Color.parseColor("#416475")));
        addSlide(AppIntroFragment.newInstance("Add new TODOs", "Add new TODOs to your saved repos with additional information like urgency and difficulty", R.drawable.addtodo, Color.parseColor("#577380")));

        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }
}
