package com.example.studolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studolist.Fragments.CalendarFragment;
import com.example.studolist.Fragments.ListFragment;
import com.example.studolist.Interfaces.CallBack_FocusDate;
import com.example.studolist.Models.Event;
import com.example.studolist.Utilities.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.Date;

public class EventActivity extends AppCompatActivity {


    private ListFragment listFragment;
    private CalendarFragment calendarFragment;


    private FloatingActionButton add_event_fab;
    private CalendarView calendarView;
    private Date mDate;
    private EditText title;
    private Button save_btn;

    Calendar calendar = Calendar.getInstance();

    SharedPreferences sharedPreferences;
    private boolean nightMode;

    private Dialog myDialog;


    CallBack_FocusDate callBack_focusDate = new CallBack_FocusDate() {
        @Override
        public void focusDate(Date mDate)
        {
            calendarFragment.focusDateOnCalendar(mDate);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(ThemeActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(ThemeActivity.NIGHT, false);
        Utility.updateTheme(this, nightMode);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        myDialog = new Dialog(this);
        initFragments();
        beginTransactions();

        add_event_fab = findViewById(R.id.add_event_fab);
        add_event_fab.setOnClickListener(v -> showPopup(v));


    }


    public void showPopup(View v) {

        myDialog.setContentView(R.layout.popup_create_event);

        TextView txtclose;
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        calendarView =(CalendarView)myDialog.findViewById(R.id.calendarView_event_popup);
        title =(EditText) myDialog.findViewById(R.id.editText_event);
        save_btn =(Button) myDialog.findViewById(R.id.save_btn_event);

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMoth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMoth);
            mDate = calendar.getTime();

        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        save_btn.setOnClickListener(v1 -> saveEvent());
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void saveEvent() {

        String t = title.getText().toString();
        Event myEvent = null;
        if (!TextUtils.isEmpty(t) && mDate != null) {

            myEvent = new Event(t, mDate);
            saveEventToFirebase(myEvent);
            myDialog.dismiss();
        } else {
            Snackbar.make(save_btn, "Fill the required details", Snackbar.LENGTH_LONG)
                    .show();

            return;
        }
    }
    public void saveEventToFirebase(Event event) {
        DocumentReference documentReference;
        //create new note
        documentReference = Utility.loadTaskFromDb_events().document();
        documentReference.set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    //event is added
                    Utility.showToast(EventActivity.this, "Event added successfully");

                } else {
                    Utility.showToast(EventActivity.this, "Failed while adding event");
                }
            }

        });

    }
    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_calendar, calendarFragment).commit();
    }

    private void initFragments() {
        listFragment = new ListFragment();
        listFragment.setCallBack(callBack_focusDate);
        calendarFragment = new CalendarFragment();
    }
}