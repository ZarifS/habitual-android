package com.closedbracket.trackit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addHabit(View view){
        Log.i("Button Clicked","Bringing to next Page");
        Intent intent = new Intent(this, AddHabit.class);
        startActivity(intent);
    }
}
