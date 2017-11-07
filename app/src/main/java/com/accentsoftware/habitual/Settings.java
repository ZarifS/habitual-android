package com.accentsoftware.habitual;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Zarif on 2017-08-22.
 */

public class Settings extends AppCompatActivity {
    private Switch rSwitch;
    private LinearLayout help;
    private LinearLayout about;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        setLayout();
    }

    private void setLayout() {
        rSwitch = (Switch) findViewById(R.id.reminderToggle);
        boolean rem = sharedPref.getBoolean("reminders", true);
        final SharedPreferences.Editor edit = sharedPref.edit();
        rSwitch.setChecked(rem);
        help = (LinearLayout) findViewById(R.id.helpSetting);
        about = (LinearLayout) findViewById(R.id.aboutSetting);

        rSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "Daily reminders are on.", Toast.LENGTH_SHORT).show();
                    setDefaultHabitReminder(true);
                    edit.putBoolean("reminders", true);
                    edit.apply();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Daily reminders are off.", Toast.LENGTH_SHORT).show();
                    setDefaultHabitReminder(false);
                    edit.putBoolean("reminders", false);
                    edit.apply();
                }
            }
        });

    }

    private void setDefaultHabitReminder(boolean flag) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("id", 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (flag){
            Calendar cal = Calendar.getInstance();
            //set a reminder every day at 9.
            cal.set(Calendar.HOUR,9);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.AM_PM,Calendar.PM);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            Log.i("setDefaultHabitReminder", "Reminder set.");

        }
        else {
            alarmManager.cancel(pendingIntent);
            Log.i("setDefaultHabitReminder", "Reminder cancelled.");

        }

    }

    public void helpClick(View view){
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);    }

    public void aboutClick(View view){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);    }

    public void feedbackClick(View view){
        Intent intent = new Intent(this, Feedback.class);
        startActivity(intent);
    }

}
