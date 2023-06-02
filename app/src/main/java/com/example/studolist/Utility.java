package com.example.studolist;


import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.example.studolist.Models.Priority;
import com.example.studolist.Models.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static CollectionReference loadTaskFromDb_tasks()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("tasks")
                .document(currentUser.getUid()).collection("my_tasks");
    }

    public static CollectionReference loadTaskFromDb_events()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("events")
                .document(currentUser.getUid()).collection("my_events");
    }
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");

        return  simpleDateFormat.format(date);

    }

    static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

    static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static int priorityColor(Task task) {
        int color;
        if (task.getPriority() == Priority.HIGH) {
            color = Color.argb(150,201, 21, 23);
        }else if (task.getPriority() == Priority.MEDIUM) {
            color = Color.argb(150,255, 179,0);
        }else {
            color = Color.argb(150, 51, 181, 229);
        }
        return color;
    }


}
