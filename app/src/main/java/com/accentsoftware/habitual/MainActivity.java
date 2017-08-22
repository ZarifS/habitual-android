package com.accentsoftware.habitual;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RealmResults<Habit> habitsResults;
    private Realm realm;
    private ListView mListView;
    HabitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultHabitReminder();
    }

    private void setDefaultHabitReminder() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("id", 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        //set a reminder every day at 9.
        cal.set(Calendar.HOUR,9);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.AM_PM,Calendar.PM);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.i("setDefaultHabitReminder", "Reminder set.");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Realm.init(this);
//        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
//        Realm.deleteRealm(configuration); //For flushing Realm DB
        realm = Realm.getDefaultInstance();
        getHabits();
        resetTracker();
        initListView();
        RealmChangeListener changeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                adapter.notifyDataSetChanged();
            }
        };
        habitsResults.addChangeListener(changeListener);
    }

    private void initListView() {
        adapter = new HabitAdapter(this,habitsResults);
        mListView = (ListView) findViewById(R.id.habits_list);
        mListView.setAdapter(adapter);
    }

    public void addHabit(View view){
        Log.i("Button Clicked","Bringing to next Page");
        Intent intent = new Intent(this, AddHabit.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            Log.i("Return to Main Habit:","Got back data from AddHabit activity.");
        }
    }

    private void getHabits(){
        habitsResults = realm.where(Habit.class).findAllSorted("name");
        Log.i("Get Habits", "Got all habits");
    }

    private String dayToString (int day){
        String res ="";
        switch (day) {
            case Calendar.SUNDAY:
                res = "Su";
                break;
            case Calendar.MONDAY:
                res = "M";
                break;
            case Calendar.TUESDAY:
                res = "T";
                break;
            case Calendar.WEDNESDAY:
                res = "W";
                break;
            case Calendar.THURSDAY:
                res = "Th";
                break;
            case Calendar.FRIDAY:
                res = "F";
                break;
            case Calendar.SATURDAY:
                res = "S";
                break;
        }
        return res;
    }

    private void resetTracker(){
        Calendar cal = Calendar.getInstance();
        int currentWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        int weekOfYear = sharedPreferences.getInt("weekOfYear", 0);

        if(weekOfYear != currentWeekOfYear){ //different week
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("weekOfYear", currentWeekOfYear);
            editor.apply();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Habit habit : habitsResults) {
                            habit.setTracker(0);
                            if(habit.isWeeklyCompletion()){
                                Log.i("Completion", ""+habit.getName()+" was completed last week.");
                                habit.setWeeklyCompletion(false);
                            }
                            else {
                                habit.setCompletion(0);
                            }
                        }
                    }
                });
        }
    }

    public void settingsClick(View view){
        Log.i("Settings Clicked","Bringing to next Page");
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
