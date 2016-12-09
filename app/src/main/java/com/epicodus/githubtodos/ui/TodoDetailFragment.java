package com.epicodus.githubtodos.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Todo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoDetailFragment extends Fragment {
    @Bind(R.id.todoTitleTextView) TextView mTodoTitleTextView;
    @Bind(R.id.createdTextView) TextView mCreatedTextView;
    @Bind(R.id.bodyTextView) TextView mBodyTextView;
    @Bind(R.id.websiteTextView) TextView mWebsiteTextView;
    @Bind(R.id.addToFirebaseButton) Button mAddToFirebaseButton;

    private Todo mTodo;

    public static TodoDetailFragment newInstance(Todo todo) {
        TodoDetailFragment todoDetailFragment = new TodoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("todo", Parcels.wrap(todo));
        todoDetailFragment.setArguments(args);
        return todoDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTodo = Parcels.unwrap(getArguments().getParcelable("todo"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_todo_detail, container, false);
        ButterKnife.bind(this, view);
        mTodoTitleTextView.setText(mTodo.getTitle());
        mBodyTextView.setText(mTodo.getBody());
        mCreatedTextView.setText(mTodo.getCreated());
        mWebsiteTextView.setText(mTodo.getUrl());

        mAddToFirebaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseReference todoRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE);
            }
        });
        return view;
    }

}
