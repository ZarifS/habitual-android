package com.closedbracket.trackit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
        Realm.init(this);
//        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
//        Realm.deleteRealm(configuration); //For flushing Realm DB
        realm = Realm.getDefaultInstance();
        getHabits();
        for(Habit habit : habitsResults) {
            Log.i("Habit name", habit.toString());
        }
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
        getHabits();
    }

    private void getHabits(){
        habitsResults = realm.where(Habit.class).findAllSorted("name");
        Log.i("Get Habits", "Got all habits");
    }
}
