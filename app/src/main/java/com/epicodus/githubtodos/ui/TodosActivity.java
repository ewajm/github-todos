package com.epicodus.githubtodos.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.githubtodos.Constants;
import com.epicodus.githubtodos.R;
import com.epicodus.githubtodos.models.Repo;
import com.epicodus.githubtodos.models.Todo;
import com.epicodus.githubtodos.services.GithubService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    private static final String TAG = TodosActivity.class.getSimpleName();
    @Bind(R.id.projectNameView) TextView mProjectNameView;
    @Bind(R.id.todoListView) ListView mTodoListView;
    @Bind(R.id.websiteUrlView) TextView mWebsiteUrlView;
    private ArrayList<Todo> mTodoArray;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTodoTitles;
    private Repo mRepo;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mCurrentUsername;
    private boolean mGithub;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRepo = Parcels.unwrap(intent.getParcelableExtra("repo"));
        mGithub = intent.getBooleanExtra("github", false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCurrentUsername = mSharedPreferences.getString(Constants.PREFERENCES_USERNAME_KEY, null);
        mProjectNameView.setText(mRepo.getName());
        mWebsiteUrlView.setText(mRepo.getUrl());
        Typeface sciFont = Typeface.createFromAsset(getAssets(), "fonts/SciFly-Sans.ttf");
        mProjectNameView.setTypeface(sciFont);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mWebsiteUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRepo.getUrl()));
                startActivity(webIntent);
            }
        });
        if(mGithub){
            checkFirebaseForRepo();
            getTodos(mRepo.getName());
        }
    }

    private void checkFirebaseForRepo() {
        Query repoQuery = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_REPOS_REFERENCE).child(mUserId).orderByChild("url").equalTo(mRepo.getUrl());
        repoQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
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
                        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(TodosActivity.this, TodoDetailActivity.class);
                                intent.putExtra("todos", Parcels.wrap(mTodoArray));
                                intent.putExtra("position", i);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }



//    @Override
//    public void onClick(View view) {
//        if(mAddTodoInput.getText().toString().length() > 0){
//            Todo newTodo = new Todo();
//            newTodo.setTitle("TODO: " + mAddTodoInput.getText().toString());
//            mTodoArray.add(newTodo);
//            if(mTodoTitles.get(0).equals("No TODOs yet! Why not add one?")){
//                mTodoTitles.remove(0);
//            }
//            mTodoTitles.add(newTodo.getTitle());
//            mAdapter.notifyDataSetChanged();
//
//            //for hiding the keyboard after button is pressed
//            InputMethodManager imm = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(mAddTodoInput.getWindowToken(), 0);
//            mAddTodoInput.setText("");
//        } else {
//            Toast.makeText(TodosActivity.this, "Please enter a todo first!", Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mGithub){
            inflater.inflate(R.menu.menu_save, menu);
            ButterKnife.bind(this);
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
                mRepo.setUid(mUserId);
                pushRef.setValue(mRepo);
                Toast.makeText(this, "Repo saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You have already saved this Repo", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
