package com.epicodus.githubtodos.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.adapters.FirebaseTodoListAdapter;
import com.epicodus.githubtodos.adapters.SavedTodoViewHolder;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.utils.OnStartDragListener;
import com.epicodus.githubtodos.utils.SimpleItemTouchHelperCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedTodosFragment extends Fragment implements OnStartDragListener {
    @Bind(R.id.todoRecyclerView) RecyclerView mTodoListView;
    @Bind(R.id.emptyView) TextView mEmptyView;
    private ArrayList<Todo> mTodoArray = new ArrayList<>();
    private Repo mRepo;
    private String mUserId;
    private FirebaseTodoListAdapter mFirebaseAdapter;
    private Query mTodoRef;
    private ItemTouchHelper mItemTouchHelper;

    public SavedTodosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepo = Parcels.unwrap(getArguments().getParcelable("repo"));
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Instructs fragment to include menu options:
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_todos, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mTodoRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).orderByChild("repoId").equalTo(mRepo.getPushId());
        mFirebaseAdapter = new FirebaseTodoListAdapter(Todo.class, R.layout.saved_todo_list_item, SavedTodoViewHolder.class, mTodoRef, this, getActivity(), mRepo);
        mTodoListView.setHasFixedSize(true);
        mTodoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTodoListView.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mTodoListView);
    }


    @Override
    public void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.cleanup();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(getActivity(), AddTodoActivity.class);
            intent.putExtra("repo", Parcels.wrap(mRepo));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
