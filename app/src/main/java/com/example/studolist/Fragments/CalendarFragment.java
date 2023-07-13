package com.example.studolist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studolist.R;

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
        calendarView.setMinDate(System.currentTimeMillis());


    }
    public void focusDateOnCalendar(Date mDate)
    {
        calendar.setTime(mDate);
       calendarView.setDate(mDate.getTime());

    }

    private void findViews(@NonNull View view) {
        calendarView =view.findViewById(R.id.calendarView_frag);
    }

}


