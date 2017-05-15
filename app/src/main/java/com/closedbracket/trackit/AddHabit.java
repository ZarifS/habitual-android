package com.closedbracket.trackit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;


public class AddHabit extends AppCompatActivity {

    private Switch mySwitch;
    private View time;
    private SeekBar weeklyTarget;
    private TextView targetText;
    private String repeatDays ="";
    private TextView targetDays;
    private String habitName;
    private int habitTarget;
    private Date habitReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        repeatDays="Daily";
        setRepeat(repeatDays); //Default repeat is daily
        initSwitch();
        initSeekBar();
    }

    private void setRepeat (String repeat) {
        targetDays = (TextView) findViewById(R.id.repeat_holder);
        targetDays.setText(repeat);
    }

    private void initSwitch(){
        time = findViewById(R.id.time_layout);
        mySwitch = (Switch) findViewById(R.id.reminder_switch);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    time.setVisibility(View.VISIBLE);
                }
                else{
                    time.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initSeekBar(){
        // Initialize the textview with OFF.
        targetText = (TextView) findViewById(R.id.seekbar_text);
        weeklyTarget = (SeekBar) findViewById(R.id.target_seekbar);
        targetText.setText("OFF");

        weeklyTarget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(progress == 0) {
                    targetText.setText("OFF");
                }
                else {
                    targetText.setText(""+progress+"");
                }
            }
        });
    }

    public void add(View view){
        Log.i("Add Button Clicked", "Adding habit to database");
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        String day = dayToString(currentDay);
        Log.i("Current day is", day);
        setAttributes();
        createNewHabit();
        Log.i("Add Button:", "Finishing activity.");
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void createNewHabit() {
        Log.i("Creating New Habit", "Adding habit attributes");
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        // increment index
        Number currentIdNum = realm.where(Habit.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        Habit habit = realm.createObject(Habit.class, nextId);
        habit.setCreated(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date dateBeforeCreated = cal.getTime();
        habit.setUpdated(dateBeforeCreated);
        habit.setName(habitName);
        habit.setRepeat(repeatDays);
        habit.setTarget(habitTarget);
        realm.commitTransaction();
        Toast.makeText(this, "Habit Added!", Toast.LENGTH_SHORT).show();
        Log.i("Creating New Habit", "Added habit finished");
    }

    private void setAttributes(){
        EditText nameHeader = (EditText) findViewById(R.id.habitName);
        habitName = nameHeader.getText().toString();
        Log.i("Habit name", habitName);
        Log.i("Habit repeat", repeatDays);
        if(targetText.getText().toString() == "OFF") {
            habitTarget = 0;
        }
        else {
            habitTarget = Integer.parseInt(targetText.getText().toString());
        }
        Log.i("Habit target", Integer.toString(habitTarget));

        // TODO - Get reminderds to be persisted with notifcations
//        if (mySwitch.isChecked()){
//            WheelPicker timeHour = (WheelPicker) findViewById(R.id.wheel_hours);
//            WheelPicker timeMin = (WheelPicker) findViewById(R.id.wheel_minutes);
//            WheelPicker timeDay = (WheelPicker) findViewById(R.id.wheel_time);
//            timeHour.getCurrentItemPosition();
//        }
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

    public void repeatClick(View view){
        Intent intent = new Intent(this, Repeat.class);
        startActivityForResult(intent,1);
        Log.i("Repeat", "Bringing to next page");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            repeatDays = intent.getStringExtra("result");
            Log.i("Return:","Got back data " + repeatDays + " from repeat activity.");
            setRepeat(repeatDays); // update repeat
        }
    }
}
