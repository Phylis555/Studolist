package com.example.studolist.Models;

import java.util.Calendar;
import java.util.Date;

public class Event {
    private String mTitle;
    private Date mDate;

    public Event() {    }
    public Event(String title, Date date) {
        mTitle = title;
        mDate = date;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }


    @Override
    public String toString() {
        return "Event{" +
                "mTitle='" + mTitle + '\'' +
                ", mDate=" + mDate +
                '}';
    }
}
