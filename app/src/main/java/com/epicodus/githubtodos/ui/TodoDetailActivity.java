package com.epicodus.githubtodos.ui;

import android.app.Activity;
import android.os.Bundle;

import com.epicodus.githubtodos.R;

public class TodoDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
    }
}
