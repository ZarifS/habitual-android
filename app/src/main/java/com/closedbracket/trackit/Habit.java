package com.closedbracket.trackit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Zarif on 2017-05-09.
 */

public class Habit extends RealmObject{
    @PrimaryKey
    private static int id=0;
    private String name;
    private Calendar created = Calendar.getInstance(); //date of habit creation.
    private ArrayList<Calendar> repeat;
    private Calendar updated;
    private Date reminder;
    private int target;
    private int tracker;

    public Habit (String name, ArrayList<Calendar> repeat, Date reminder, int target, int tracker){
        id = id+1;
        this.name = name;
        this.repeat = repeat;
        this.reminder=reminder;
        this.target=target;
        this.tracker=tracker;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Habit.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public ArrayList<Calendar> getRepeat() {
        return repeat;
    }

    public void setRepeat(ArrayList<Calendar> repeat) {
        this.repeat = repeat;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTracker() {
        return tracker;
    }

    public void setTracker(int tracker) {
        this.tracker = tracker;
    }

    @Override
    public String toString() {
        String temp;
        String rep ="";
        if(repeat.size() == 7){
            rep = "Daily";
        }
        else{
            for (int i=0; i<repeat.size(); i++){
                rep = rep + repeat.get(i).get(Calendar.DAY_OF_WEEK) + " ";
            }
        }
        temp = "Habit Name:" + getName() + ", Repeating: " + rep + ", Target: " + getTarget() + ", On track for: " + getTracker();
        return temp;
    }
}
