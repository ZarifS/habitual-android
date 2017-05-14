package com.closedbracket.trackit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import io.realm.RealmResults;

/**
 * Created by Zarif on 2017-05-13.
 */

public class HabitAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private RealmResults<Habit> mDataSource;

    public HabitAdapter(Context context, RealmResults<Habit> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.list_item_habit, parent, false);

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


        //Get corresponding recipe for row
        Habit habit = (Habit) getItem(position);

        // Update row view's textviews to display recipe information
        habitName.setText(habit.getName());
        habitTracker.setText(Integer.toString(habit.getTracker()));
        return convertView;
    }

    private static class ViewHolder {
        public TextView habitName;
        public ImageButton habitChecked;
        public Button habitTracker;
    }
}
