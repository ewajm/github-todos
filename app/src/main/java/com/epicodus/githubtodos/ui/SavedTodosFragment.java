package com.epicodus.githubtodos.ui;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.githubtodos.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SavedTodosFragment extends Fragment {


    public SavedTodosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_todos, container, false);
    }

}
