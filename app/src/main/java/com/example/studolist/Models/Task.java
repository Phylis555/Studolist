package com.example.studolist.Models;

import android.net.Uri;

import java.util.Calendar;
import java.util.Date;

public class Task {



    private String task;
    private Priority priority;
    private Date dueDate;
    private Date dataCreated;


    private String imgUri;

    public Task(String task, Priority priority, Date dueDate, Date dataCreated, String imgUri) {
        this.task = task;
        this.priority = priority;
        this.dueDate = dueDate;
        this.dataCreated = dataCreated;
        this.imgUri = imgUri;
    }

    public Task() {
        this.task = "";
        this.priority = Priority.a_LOW;
        this.dueDate = Calendar.getInstance().getTime();
        this.dataCreated = Calendar.getInstance().getTime();
        this.imgUri = "";
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Date dataCreated) {
        this.dataCreated = dataCreated;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUrl) {
        imgUri = imgUrl;
    }

    public String toShareStr()
    {
        return  "The task is " + task +"," +
                " with a priority level of " + priority+
                ". It is due on " + dueDate +
                "and was created on " + dataCreated +
                ".The image URI associated with the task is: \n" + imgUri
                ;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task='" + task + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", dataCreated=" + dataCreated +
                ", imgUri=" + imgUri +
                '}';
    }
}
