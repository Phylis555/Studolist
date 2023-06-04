package com.example.studolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studolist.Models.Priority;
import com.example.studolist.Models.Task;
import com.example.studolist.Utilities.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

public class TaskDetailsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Calendar calendar = Calendar.getInstance();
    private Priority priority;

    private EditText enterTodo;
    private ImageButton calendarBtn;
    private ImageButton priorityBtn;
    private ImageButton addImageBtn;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private  String imageUrl;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioBtn;
    private int selectedBtnId;
    private Date dueDate;
    private ImageButton saveBtn;
    private CalendarView calendarView;
    private String docId, taskEdit;
    private long  dueDateEdit;
    private int priorityEdit;
    private TextView pageTitleTxt;
    private TextView deleteBtn;
    private StorageReference storageReference;
    private boolean isEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        findViews();
        initViews();
        calendarView.setMinDate(System.currentTimeMillis());
        storageReference = FirebaseStorage.getInstance().getReference("tasks_images");

        //recieve data
        taskEdit= getIntent().getStringExtra("task");
        dueDateEdit= getIntent().getLongExtra("dueDate",0);
        priorityEdit= getIntent().getIntExtra("priority",0);
        docId = getIntent().getStringExtra("docId");
/////////// Try yo make it short :
        if(docId!=null && !docId.isEmpty()){
            isEdit = true;
        }
        if(isEdit)
        {
            calendarView.setDate(dueDateEdit);
            if (priorityEdit == Priority.c_HIGH.ordinal() ) {
            selectedRadioBtn = findViewById(R.id.radioButton_high);
            selectedRadioBtn.setChecked(true);
        } else if (priorityEdit ==Priority.b_MEDIUM.ordinal()) {
            selectedRadioBtn = findViewById(R.id.radioButton_med);
            selectedRadioBtn.setChecked(true);
        } else if (priorityEdit ==Priority.a_LOW.ordinal()) {
            selectedRadioBtn = findViewById(R.id.radioButton_low);
            selectedRadioBtn.setChecked(true);
        } else {
            priority = Priority.a_LOW;
        }
            enterTodo.setText(taskEdit);
            calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMoth) -> {
                calendar.clear();
                calendar.set(year, month, dayOfMoth);
                dueDate = calendar.getTime();

            });
        }

        ///////////

        if(isEdit){
            pageTitleTxt.setText("Edit your task");
            deleteBtn.setVisibility(View.VISIBLE);
        }


    }


    public void findViews() {
        calendarView = findViewById(R.id.calendar_view);
        calendarBtn = findViewById(R.id.today_calendar_button);
        enterTodo = findViewById(R.id.enter_todo_et);
        saveBtn = findViewById(R.id.save_todo_button);
        priorityBtn = findViewById(R.id.priority_todo_button);
        priorityRadioGroup = findViewById(R.id.radioGroup_priority);
        pageTitleTxt = findViewById(R.id.page_title);
        deleteBtn = findViewById(R.id.delete_task_btn);
        addImageBtn = findViewById(R.id.img_todo_button);
        mProgressBar = findViewById(R.id.progress_bar);
        
    }


    public void initViews() {
        calendarView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.INVISIBLE);
        calendarBtn.setOnClickListener(view12 -> {
            calendarView.setVisibility(calendarView.getVisibility() == View.GONE ?
                    View.VISIBLE : View.GONE);
            // Utils.hideSoftKeyboard(view12);

        });
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMoth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMoth);
            if(!isEdit)
                dueDate = calendar.getTime();

        });

        priorityBtn.setOnClickListener(view13 -> {
            priorityRadioGroup.setVisibility(
                    priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
        });
        priority = Priority.a_LOW;
        priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
                selectedBtnId = checkedId;
                selectedRadioBtn = findViewById(selectedBtnId);
                if (selectedRadioBtn.getId() == R.id.radioButton_high) {
                    priority = Priority.c_HIGH;
                } else if (selectedRadioBtn.getId() == R.id.radioButton_med) {
                    priority = Priority.b_MEDIUM;
                } else if (selectedRadioBtn.getId() == R.id.radioButton_low) {
                    priority = Priority.a_LOW;
                } else {
                    priority = Priority.a_LOW;
                }
            } else {
                priority = Priority.a_LOW;
            }
        });
        addImageBtn.setOnClickListener(v -> {
            openFileChooser();
        });

        saveBtn.setOnClickListener((v) -> saveTask());
        deleteBtn.setOnClickListener((v -> deleteTaskFromFirebase()));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            //Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void deleteTaskFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.loadTaskFromDb_tasks().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(TaskDetailsActivity.this,"Task deleted successfully");
                    finish();
                }else{
                    Utility.showToast(TaskDetailsActivity.this,"Failed while deleting task");
                }
            }
        });
    }

    public void saveTask() {

        String task = enterTodo.getText().toString().trim();

        Task myTask = new Task();
        if (!TextUtils.isEmpty(task)) {
            if (mImageUri != null) {
                saveTaskToStorage();
            }else {
                myTask.setTask(task);
                myTask.setPriority(priority);
                if(dueDate!=null)
                    myTask.setDueDate(dueDate);
                myTask.setImgUri(imageUrl);
                saveTaskToFirebase(myTask);
            }


        } else {
            Snackbar.make(saveBtn, "Empty task", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }





}
    public void saveTaskToStorage()
    {
        mProgressBar.setVisibility(View.VISIBLE);

        final StorageReference filepath = storageReference //.../tasks_images/our_image.jpeg
                .child("tasks_images")
                .child("my_image_" + System.currentTimeMillis() + "." + getFileExtension(mImageUri)); // my_image_887474737

        filepath.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setProgress(0);
                            }
                        }, 500);

                        Toast.makeText(TaskDetailsActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                Task myTask = new Task();
                                String task = enterTodo.getText().toString().trim();
                                myTask.setTask(task);
                                myTask.setPriority(priority);
                                if(dueDate !=null)
                                    myTask.setDueDate(dueDate);
                                myTask.setImgUri(imageUrl);
                                saveTaskToFirebase(myTask);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TaskDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    }
                });

    }
    public void saveTaskToFirebase(Task task) {
        DocumentReference documentReference;
        if (isEdit) {
            //update the note
            documentReference = Utility.loadTaskFromDb_tasks().document(docId);
        } else {
            //create new note
            documentReference = Utility.loadTaskFromDb_tasks().document();
        }

        documentReference.set(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                if (task.isSuccessful()) {
                    //task is added
                    if(isEdit)
                        Utility.showToast(TaskDetailsActivity.this, "Task edited successfully");
                    else
                        Utility.showToast(TaskDetailsActivity.this, "Task added successfully");
                    finish();
                } else {
                    Utility.showToast(TaskDetailsActivity.this, "Failed while adding task");
                }
            }

        });


    }
}