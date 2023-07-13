package com.example.studolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.studolist.Adapters.TaskAdapter;
import com.example.studolist.Models.Task;
import com.example.studolist.Utilities.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton addTaskFab;
    FloatingActionButton addEventFab;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    ImageButton sortBtn;
    TaskAdapter taskAdapter;
    Chip camChip;
    SharedPreferences sharedPreferences;
    private boolean nightMode;
    private Query query;

    private FirestoreRecyclerOptions options;
    private Query.Direction direction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences(ThemeActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(ThemeActivity.NIGHT, false);

        Utility.updateTheme(this, nightMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setSupportActionBar(toolbar);
        initViews();

    }




    private void initViews() {

        addTaskFab.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, TaskDetailsActivity.class)));
        addEventFab.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, EventActivity.class)));
        menuBtn.setOnClickListener((v -> showMenu()));
        sortBtn.setOnClickListener((v -> showSortOptions()));
        setupRecyclerView();
    }



    private void showSortOptions() {
        PopupMenu popupSort  = new PopupMenu(MainActivity.this,sortBtn);
        popupSort.getMenu().add("dataCreated");
        popupSort.getMenu().add("dueDate");
        popupSort.getMenu().add("priority");
        popupSort.show();
        popupSort.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()!="priority") {
                    updateSortOrder(menuItem.getTitle().toString(), Query.Direction.ASCENDING);
                }
                else {
                    updateSortOrder(menuItem.getTitle().toString(), Query.Direction.DESCENDING);
                }
                return true;
            }
        });
    }


    private void updateSortOrder(String field, Query.Direction direction) {
        query = Utility.loadTaskFromDb_tasks().orderBy(field, direction);
        options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        taskAdapter.updateOptions(options);
        taskAdapter.notifyDataSetChanged();


    }
    private void setupRecyclerView() {
         query  = Utility.loadTaskFromDb_tasks().orderBy("dataCreated",Query.Direction.DESCENDING);
         options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query,Task.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TaskAdapter(options,this);
        recyclerView.setAdapter(taskAdapter);
    }


    private void showMenu() {
        PopupMenu popupMenu  = new PopupMenu(MainActivity.this,menuBtn);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        popupMenu.getMenu().add("Username: " +user.getDisplayName());
        popupMenu.getMenu().add("Theme");
        popupMenu.getMenu().add("About");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                if(menuItem.getTitle()=="Theme"){
                    startActivity(new Intent(MainActivity.this,ThemeActivity.class));
                    return true;
                }
                if(menuItem.getTitle()=="About"){
                    //Start new activity
                    return true;
                }
                return false;
            }
        });

    }




    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        addTaskFab = findViewById(R.id.add_task_fab);
        addEventFab = findViewById(R.id.event_fab);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
        sortBtn = findViewById(R.id.sort_btn);
        camChip = findViewById(R.id.cam_chip);
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskAdapter.notifyDataSetChanged();
    }
}