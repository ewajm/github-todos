package com.epicodus.githubtodos.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.services.GithubService;
import com.epicodus.githubtodos.utils.DatabaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodosFragment extends Fragment {
    public static final String TAG = TodosActivity.class.getSimpleName();
    @Bind(R.id.todoListView) ListView mTodoListView;
    private ArrayList<Todo> mTodoArray = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTodoTitles;
    private Repo mRepo;
    private SharedPreferences mSharedPreferences;
    private String mCurrentUsername;
    private boolean mGithub;
    private String mUserId;
    private Query mRepoQuery;
    private int mOrientation;

    public TodosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mRepo = Parcels.unwrap(getArguments().getParcelable("repo"));
        // Instructs fragment to include menu options:
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_todos, container, false);
        ButterKnife.bind(this, view);
        mCurrentUsername = mSharedPreferences.getString(Constants.PREFERENCES_USERNAME_KEY, null);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mOrientation = view.getResources().getConfiguration().orientation;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkFirebaseForRepo();
        getTodos(mRepo.getName());
    }

    private void checkFirebaseForRepo() {
        mRepoQuery = DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId).orderByChild("url").equalTo(mRepo.getUrl());
        mRepoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mRepo.setPushId(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTodos(String repoName){
        final GithubService githubService = new GithubService();
        githubService.getRepoIssues(repoName, mCurrentUsername, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
                mTodoArray = githubService.processIssueResponse(response);
                mTodoTitles = new ArrayList<>();
                for(int i = 0; i < mTodoArray.size(); i++){
                    mTodoTitles.add(mTodoArray.get(i).getTitle());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTodoListView.setEmptyView(getView().findViewById(android.R.id.empty));
                        mAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_todo_list_item, mTodoTitles);
                        mTodoListView.setAdapter(mAdapter);
                        mTodoListView.setOnItemClickListener(todoListItemClickListener());
                        if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                            createDetailFragment(0);
                        }
                    }
                });
            }
        });
    }

    private void createDetailFragment(int position) {
        TodoDetailFragment detailFragment = TodoDetailFragment.newInstance(mTodoArray.get(position), mRepo);
        // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        //  Replaces the FrameLayout with the RestaurantDetailFragment:
        ft.replace(R.id.todoDetailFrameLayout, detailFragment);
        // Commits these changes:
        ft.commit();
    }

    @NonNull
    private AdapterView.OnItemClickListener todoListItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                    createDetailFragment(i);
                } else {
                    Intent intent = new Intent(getActivity(), TodoDetailActivity.class);
                    intent.putExtra("todos", Parcels.wrap(mTodoArray));
                    intent.putExtra("repo", Parcels.wrap(mRepo));
                    intent.putExtra("position", i);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_repo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save_repo) {
            if (mRepo.getPushId() == null) {
                DatabaseReference repoRef = DatabaseUtil.getDatabase().getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId);
                DatabaseReference pushRef = repoRef.push();
                String pushId = pushRef.getKey();
                mRepo.setPushId(pushId);
                pushRef.setValue(mRepo);
                Toast.makeText(getActivity(), "Repo saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "You have already saved this Repo", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
