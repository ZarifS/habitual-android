package com.closedbracket.trackit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AddHabit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
    }

    public void repeatClick(View view){
        Log.i("Repeat", "Bringing to next page");
    }
}
