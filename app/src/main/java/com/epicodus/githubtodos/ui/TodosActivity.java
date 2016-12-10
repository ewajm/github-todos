package com.epicodus.githubtodos.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.services.GithubService;
import com.firebase.ui.database.FirebaseListAdapter;
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

public class TodosActivity extends BaseActivity {
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.todoListView) ListView mTodoListView;
    @Bind(R.id.websiteUrlView) TextView mWebsiteUrlView;
    private ArrayList<Todo> mTodoArray = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTodoTitles;
    private Repo mRepo;
    private SharedPreferences mSharedPreferences;
    private String mCurrentUsername;
    private boolean mGithub;
    private String mUserId;
    private Query mRepoQuery;
    private FirebaseListAdapter mFirebaseAdapter;
    private Query mTodoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        //member variable set up
        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mGithub = mSharedPreferences.getBoolean(Constants.PREFERENCES_GITHUB_TOGGLE_KEY, false);
        mCurrentUsername = mSharedPreferences.getString(Constants.PREFERENCES_USERNAME_KEY, null);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

    @Override
    protected void onStart() {
        super.onStart();
        if(mGithub){
            checkFirebaseForRepo();
            getTodos(mRepo.getName());
        } else {
            setUpFirebaseAdapter();
        }
    }

    private void setUpFirebaseAdapter() {
        mTodoRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_TODOS_REFERENCE).child(mUserId).orderByChild("repoId").equalTo(mRepo.getPushId());
        mFirebaseAdapter = new FirebaseListAdapter<Todo>(this, Todo.class, R.layout.custom_todo_list_item, mTodoRef) {
            @Override
            protected void populateView(View v, Todo model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getTitle());
            }
        };
        mTodoListView.setEmptyView(findViewById(android.R.id.empty));
        mTodoListView.setAdapter(mFirebaseAdapter);
        mTodoListView.setOnItemClickListener(todoListItemClickListener());
    }

    private void checkFirebaseForRepo() {
        mRepoQuery = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId).orderByChild("url").equalTo(mRepo.getUrl());
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
                mTodoArray = githubService.processIssueResponse(response);
                mTodoTitles = new ArrayList<>();
                for(int i = 0; i < mTodoArray.size(); i++){
                    mTodoTitles.add(mTodoArray.get(i).getTitle());
                }
                TodosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mTodoTitles.size() == 0){
                            mTodoTitles.add("No TODOs yet! Why not add one?");
                        }
                        mAdapter = new ArrayAdapter<>(TodosActivity.this, R.layout.custom_todo_list_item, mTodoTitles);
                        mTodoListView.setAdapter(mAdapter);
                        mTodoListView.setOnItemClickListener(todoListItemClickListener());
                    }
                });
            }
        });
    }

    @NonNull
    private AdapterView.OnItemClickListener todoListItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(!mGithub){
                    mTodoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                mTodoArray.add(snapshot.getValue(Todo.class));
                            }
                            //intent must be explicitly called within on data change or will fire too early
                            Intent intent = new Intent(TodosActivity.this, TodoDetailActivity.class);
                            intent.putExtra("todos", Parcels.wrap(mTodoArray));
                            intent.putExtra("repo", Parcels.wrap(mRepo));
                            intent.putExtra("position", i);
                            startActivity(intent);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Intent intent = new Intent(TodosActivity.this, TodoDetailActivity.class);
                    intent.putExtra("todos", Parcels.wrap(mTodoArray));
                    intent.putExtra("repo", Parcels.wrap(mRepo));
                    intent.putExtra("position", i);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.cleanup();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mGithub){
            inflater.inflate(R.menu.menu_save, menu);
            ButterKnife.bind(this);
        } else {
            inflater.inflate(R.menu.menu_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            if(mRepo.getPushId() == null){
                DatabaseReference repoRef =  FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId);
                DatabaseReference pushRef = repoRef.push();
                String pushId = pushRef.getKey();
                mRepo.setPushId(pushId);
                pushRef.setValue(mRepo);
                Toast.makeText(this, "Repo saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You have already saved this Repo", Toast.LENGTH_SHORT).show();
            }
        } else if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(this, AddTodoActivity.class);
            intent.putExtra("repo", Parcels.wrap(mRepo));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
