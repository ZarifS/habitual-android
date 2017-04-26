package com.closedbracket.trackit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


public class AddHabit extends AppCompatActivity {

    private Switch mySwitch;
    private View time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
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

    public void repeatClick(View view){
        Log.i("Repeat", "Bringing to next page");
    }
}
