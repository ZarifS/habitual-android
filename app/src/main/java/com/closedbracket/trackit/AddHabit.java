package com.closedbracket.trackit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class AddHabit extends AppCompatActivity {

    private Switch mySwitch;
    private View time;
    private SeekBar weeklyTarget;
    private TextView targetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        initSwitch();
        initSeekBar();
    }

    public void initSwitch(){
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

    public void initSeekBar(){
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

    public void repeatClick(View view){
        Log.i("Repeat", "Bringing to next page");
    }
}
