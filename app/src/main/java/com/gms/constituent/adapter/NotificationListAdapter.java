package com.gms.constituent.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.Meeting;
import com.gms.constituent.bean.support.Notification;

import java.util.ArrayList;

public class NotificationListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<Notification> notifications;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();


    public NotificationListAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
//        Collections.reverse(services);
//        transformation = new RoundedTransformationBuilder()
//                .cornerRadiusDp(0)
//                .oval(false)
//                .build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if (mSearching) {
            if (!mAnimateSearch) {
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();
        } else {
            return notifications.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return notifications.get(mValidSearchIndices.get(position));
        } else {
            return notifications.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NotificationListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_notification, parent, false);

            holder = new NotificationListAdapter.ViewHolder();
            holder.txtNotificationTime = (TextView) convertView.findViewById(R.id.notification_time);
            holder.txtNotificationText = (TextView) convertView.findViewById(R.id.notification_detail);
            holder.txtNotificationDate = (TextView) convertView.findViewById(R.id.notification_date);
            convertView.setTag(holder);
        } else {
            holder = (NotificationListAdapter.ViewHolder) convertView.getTag();
            holder.txtNotificationTime = (TextView) convertView.findViewById(R.id.notification_time);
            holder.txtNotificationText = (TextView) convertView.findViewById(R.id.notification_detail);
            holder.txtNotificationDate = (TextView) convertView.findViewById(R.id.notification_date);
        }
        holder.txtNotificationTime.setText(notifications.get(position).getcreated_time());
        holder.txtNotificationText.setText(capitalizeString(notifications.get(position).getnotification_text()));
        holder.txtNotificationDate.setText(notifications.get(position).getcreated_at());

        if (position!=0) {
            if (notifications.get(position).getcreated_at().equalsIgnoreCase((notifications.get(position-1).getcreated_at()))) {
                holder.txtNotificationDate.setVisibility(View.GONE);
            }
        }


        if (mSearching) {
            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        return convertView;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < notifications.size(); i++) {
            String homeWorkTitle = notifications.get(i).getid();
            if ((homeWorkTitle != null) && !(homeWorkTitle.isEmpty())) {
                if (homeWorkTitle.toLowerCase().contains(eventName.toLowerCase())) {
                    mValidSearchIndices.add(i);
                }
            }
        }
        Log.d("Event List Adapter", "notify" + mValidSearchIndices.size());
    }

    public void exitSearch() {
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtNotificationDate, txtNotificationTime, txtNotificationText;
    }

    public boolean ismSearching() {
        return mSearching;
    }

    public int getActualEventPos(int selectedSearchpos) {
        if (selectedSearchpos < mValidSearchIndices.size()) {
            return mValidSearchIndices.get(selectedSearchpos);
        } else {
            return 0;
        }
    }

    private void getStat(String stat) {

    }
}