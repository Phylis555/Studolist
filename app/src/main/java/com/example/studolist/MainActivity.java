package com.example.studolist;

import android.content.Intent;
import android.os.Bundle;

import com.example.studolist.Models.Priority;
import com.example.studolist.Models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton addTaskFab;
    FloatingActionButton addEventFab;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    ImageButton sortBtn;
    TaskAdapter taskAdapter;
    Chip camChip;
    private Query query;
    private FirestoreRecyclerOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                updateSortOrder(menuItem.getTitle().toString(), Query.Direction.ASCENDING);
                return true;
            }
        });
    }

    Comparator<Task> priorityComparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            Priority priority1 = task1.getPriority();
            Priority priority2 = task2.getPriority();

            // Compare the enum values to determine the sort order
            return priority1.compareTo(priority2);
        }
    };
    private void updateSortPriority() {
        //Try to order with priority
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
        popupMenu.getMenu().add(user.getDisplayName());
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