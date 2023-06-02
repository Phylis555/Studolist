package com.example.studolist.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studolist.EventActivity;
import com.example.studolist.Models.Event;
import com.example.studolist.Models.Task;
import com.example.studolist.R;
import com.example.studolist.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {


    private CalendarView calendarView;

    Calendar calendar = Calendar.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMoth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMoth);

        });
    }
    public void focusDateOnCalendar(Date mDate)
    {
        calendar.setTime(mDate);
        long selectedDateInMillis = calendar.getTimeInMillis();
        calendarView.setDate(selectedDateInMillis, true, true);

    }

    private void findViews(@NonNull View view) {
        calendarView =view.findViewById(R.id.calendarView_frag);
    }

}



