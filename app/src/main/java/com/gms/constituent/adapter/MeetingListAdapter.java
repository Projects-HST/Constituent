package com.gms.constituent.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
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
import com.gms.constituent.bean.support.News;

import java.util.ArrayList;

public class MeetingListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<Meeting> meetings;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();


    public MeetingListAdapter(Context context, ArrayList<Meeting> meetings) {
        this.context = context;
        this.meetings = meetings;
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
            return meetings.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return meetings.get(mValidSearchIndices.get(position));
        } else {
            return meetings.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_meeting, parent, false);

            holder = new ViewHolder();
            holder.totalLayout = (LinearLayout) convertView.findViewById(R.id.total_layout);
            holder.txtMeetingTitle = (TextView) convertView.findViewById(R.id.meeting_title);
            holder.txtStatusTitle = (TextView) convertView.findViewById(R.id.status_title);
            holder.txtMeetingDate = (TextView) convertView.findViewById(R.id.meeting_date);
            holder.txtMeetingStatus = (TextView) convertView.findViewById(R.id.meeting_status);
            holder.meetingImage = (ImageView) convertView.findViewById(R.id.meeting_img);
            holder.meetingDateImage = (ImageView) convertView.findViewById(R.id.meeting_date_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.totalLayout = (LinearLayout) convertView.findViewById(R.id.total_layout);
            holder.txtMeetingTitle = (TextView) convertView.findViewById(R.id.meeting_title);
            holder.txtStatusTitle = (TextView) convertView.findViewById(R.id.status_title);
            holder.txtMeetingDate = (TextView) convertView.findViewById(R.id.meeting_date);
            holder.txtMeetingStatus = (TextView) convertView.findViewById(R.id.meeting_status);
            holder.meetingImage = (ImageView) convertView.findViewById(R.id.meeting_img);
            holder.meetingDateImage = (ImageView) convertView.findViewById(R.id.meeting_date_img);
        }
        holder.txtMeetingTitle.setText(meetings.get(position).getmeeting_title());
        holder.txtMeetingDate.setText(meetings.get(position).getmeeting_date());
        holder.txtMeetingStatus.setText(meetings.get(position).getmeeting_status());

        if (meetings.get(position).getmeeting_status().equalsIgnoreCase("REQUESTED")) {
            holder.txtStatusTitle.setText("Upcoming");
            holder.txtMeetingStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.requested));
            holder.meetingImage.setImageResource(R.drawable.ic_meeting_active);
            holder.meetingDateImage.setImageResource(R.drawable.ic_date_active);
        } else if (meetings.get(position).getmeeting_status().equalsIgnoreCase("COMPLETED")){
            holder.txtStatusTitle.setText("Earlier");
            holder.txtMeetingStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.completed));
            holder.meetingImage.setImageResource(R.drawable.ic_meeting_inactive);
            holder.meetingDateImage.setImageResource(R.drawable.ic_date_inactive);
        }
        if (position!=0) {
            if (meetings.get(position).getmeeting_status().equalsIgnoreCase((meetings.get(position-1).getmeeting_status()))) {
                holder.txtStatusTitle.setVisibility(View.GONE);
            }
        }


        if (mSearching) {
            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < meetings.size(); i++) {
            String homeWorkTitle = meetings.get(i).getid();
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
        public TextView txtMeetingTitle, txtStatusTitle, txtMeetingDate, txtMeetingStatus;
        private ImageView meetingImage, meetingDateImage;
        private LinearLayout totalLayout;
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