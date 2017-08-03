package com.closedbracket.trackit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Zarif on 2017-05-13.
 */

public class HabitAdapter extends BaseSwipeAdapter{

    private Context mContext;
    private RealmResults<Habit> mDataSource;
    private Realm realm;
    private SwipeLayout swipeLayout;

    public HabitAdapter(Context context, RealmResults<Habit> items) {
        mContext = context;
        mDataSource = items;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Habit getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mDataSource.get(i).getId();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, final ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_habit, null);
        swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.delete));
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAllItems();
                deleteHabit(position);
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Edit Button", "Clicked");
            }
        });
        return v;
    }

    private void deleteHabit(int position) {
        realm.beginTransaction();
        Habit habit = mDataSource.get(position);
        Log.i("DeleteHabit:", "Deleting " + habit.getName());
        habit.deleteFromRealm();
        Log.i("DeleteHabit:", "Deleted habit");
        realm.commitTransaction();
        Toast.makeText(mContext, "Habit Deleted.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void fillValues(final int position, View convertView) {

        ViewHolder holder;

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView.getTag()==null) {
            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.habitName= (TextView) convertView.findViewById(R.id.habitlist_name);
            holder.habitTracker = (Button) convertView.findViewById(R.id.habitlist_tracker);
            holder.habitChecked = (ImageButton) convertView.findViewById(R.id.habitlist_checked);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        }
        else {
            // skip all the expensive inflation/findViewById and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get relevant subviews of row view
        TextView habitName = holder.habitName;
        Button habitTracker = holder.habitTracker;
        ImageButton habitChecked = holder.habitChecked;


        //Get corresponding habit for row
        Habit habit = getItem(position);

        // Update row view's textviews to display habit information
        habitName.setText(habit.getName());

        //Set habit tracker to show either tracker/target or just target
        if(habit.getTarget() != 0) {
            String target = Integer.toString(habit.getTarget());
            String tracker = Integer.toString(habit.getTracker());
            habitTracker.setText(tracker+"/"+target);
        }
        else habitTracker.setText(Integer.toString(habit.getTracker()));

        if (isSameDay(position)){
            habitChecked.setBackgroundResource(R.drawable.green);
        }
        else habitChecked.setBackgroundResource(R.drawable.grey); //if habit has been updated today, set color to green, else set to grey
        habitChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("In on Click:", ""+position+"");
                updateHabit(position);
            }
        });
    }

    private void updateHabit(int position){
        Realm.init(mContext);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
//        mDataSource.get(position).setTracker(mDataSource.get(position).getTracker()+1);
        if(!isSameDay(position)){ //If the habit hasn't already been updated today
            mDataSource.get(position).setTracker(mDataSource.get(position).getTracker()+1);
            mDataSource.get(position).setUpdated(new Date()); //update updated date
        }
        realm.commitTransaction();
    }

    private boolean isSameDay(int position){
        Date currentDate = new Date();
        Date updatedDate = mDataSource.get(position).getUpdated();
        // Strip out the time part of each date.
        int MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
        long julianDayNumber1 = currentDate.getTime() / MILLIS_PER_DAY;
        long julianDayNumber2 = updatedDate.getTime() / MILLIS_PER_DAY;

        // If they now are equal then it is the same day.
        return julianDayNumber1 == julianDayNumber2;
    }

    private static class ViewHolder {
        public TextView habitName;
        public ImageButton habitChecked;
        public Button habitTracker;
    }
}
