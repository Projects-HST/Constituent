package com.gms.constituent.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.Meeting;
import com.gms.constituent.utils.PreferenceStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TestMeetingListAdapter extends RecyclerView.Adapter<TestMeetingListAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Meeting> meetingArrayList;
    private ArrayList<Meeting> og;
    private ArrayList<Meeting> meetingArrayListFiltered;
    Context context;
    private OnItemClickListener onItemClickListener;
    public static int selected_item = 0;


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    meetingArrayListFiltered = og;
                    //filteredCUG = CUG;
                } else {
                    ArrayList<Meeting> filtered = new ArrayList<>();
                    for (Meeting cug : meetingArrayList) {
                        if (cug.getmeeting_title().toLowerCase().contains(charString) || cug.getmeeting_date().toLowerCase().contains(charString)) {
                            filtered.add(cug);
                        }
                    }
                    meetingArrayListFiltered = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = meetingArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //filteredCUG.clear();
                meetingArrayList = (ArrayList<Meeting>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtReqMeetTitle, txtReqConstName, txtReqMeetDate, txtSchMeetTitle, txtSchConstName,
                txtSchMeetDate, txtSchDate,  txtComMeetTitle, txtComConstName, txtComMeetDate, txtComDate;
        private LinearLayout requestLayout, scheduledLayout, completeLayout;
        private FrameLayout requestFrame, scheduleFrame, completeFrame;

        public MyViewHolder(View view) {
            super(view);
            requestLayout = (LinearLayout) view.findViewById(R.id.request_layout);
            scheduledLayout = (LinearLayout) view.findViewById(R.id.schedule_layout);
            completeLayout = (LinearLayout) view.findViewById(R.id.completed_layout);
            requestFrame = (FrameLayout) view.findViewById(R.id.meeting_req_click);
            scheduleFrame = (FrameLayout) view.findViewById(R.id.meeting_sch_click);
            completeFrame = (FrameLayout) view.findViewById(R.id.meeting_com_click);
            requestFrame.setOnClickListener(this);
            scheduleFrame.setOnClickListener(this);
            completeFrame.setOnClickListener(this);
            txtReqMeetTitle = (TextView) view.findViewById(R.id.req_meeting_title);
            txtReqConstName = (TextView) view.findViewById(R.id.req_constituency_name);
            txtReqMeetDate = (TextView) view.findViewById(R.id.req_meeting_date);
            txtSchMeetTitle = (TextView) view.findViewById(R.id.sch_meeting_title);
            txtSchConstName = (TextView) view.findViewById(R.id.sch_constituency_name);
            txtSchMeetDate = (TextView) view.findViewById(R.id.sch_meeting_date);
            txtSchDate = (TextView) view.findViewById(R.id.sch_date);
//            txtComMeetTitle = (TextView) view.findViewById(R.id.com_meeting_title);
//            txtComConstName = (TextView) view.findViewById(R.id.com_constituency_name);
//            txtComMeetDate = (TextView) view.findViewById(R.id.com_meeting_date);
//            txtComDate = (TextView) view.findViewById(R.id.com_date);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }
    public TestMeetingListAdapter(ArrayList<Meeting> meetingArrayList, Context mCtx, OnItemClickListener onItemClickListener) {
        Collections.reverse(meetingArrayList);
        this.meetingArrayList = meetingArrayList;
        this.meetingArrayListFiltered = meetingArrayList;
        this.og = meetingArrayList;
        this.context = mCtx;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public TestMeetingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_list_meeting_item, parent, false);

//        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(context));

        return new TestMeetingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TestMeetingListAdapter.MyViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);
        if (meeting.getmeeting_status().equalsIgnoreCase("REQUESTED")) {
            holder.requestLayout.setVisibility(View.VISIBLE);
            holder.scheduledLayout.setVisibility(View.GONE);
//            holder.completeLayout.setVisibility(View.GONE);
            holder.txtReqMeetTitle.setText(capitalizeString(meeting.getmeeting_title()));
            holder.txtReqConstName.setText(capitalizeString(PreferenceStorage.getConstituencyName(context) + " ( " + "Paguthi" + " )"));
            holder.txtReqMeetDate.setText(("Requested On" + " : " + meeting.getcreated_at()));
        }
        else{
            holder.requestLayout.setVisibility(View.GONE);
            holder.scheduledLayout.setVisibility(View.VISIBLE);
//            holder.completeLayout.setVisibility(View.GONE);
            holder.txtSchMeetTitle.setText(capitalizeString(meeting.getmeeting_title()));
            holder.txtSchConstName.setText(capitalizeString(PreferenceStorage.getConstituencyName(context) + " ( " + "Paguthi" + " )"));
            holder.txtSchMeetDate.setText(("Requested On" + " : " + meeting.getcreated_at()));
            holder.txtSchDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
        }
//        if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")){
//            holder.requestLayout.setVisibility(View.GONE);
//            holder.scheduledLayout.setVisibility(View.GONE);
//            holder.completeLayout.setVisibility(View.VISIBLE);
//            holder.txtComMeetTitle.setText(capitalizeString(meeting.getmeeting_title()));
//            holder.txtComConstName.setText(capitalizeString(PreferenceStorage.getConstituencyName(context) + " ( " + "Paguthi" + " )"));
//            holder.txtComMeetDate.setText(("Requested On" + " : " + meeting.getcreated_at()));
//            holder.txtComDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
//        }

        if (position != 0) {
            if (checkdatapos(position)) {
//                holder.txtStatusTitle.setVisibility(View.GONE);
            } else {
//                holder.txtStatusTitle.setVisibility(View.VISIBLE);
            }
        } else {
//            holder.txtStatusTitle.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkdatapos(int position) {
        Meeting meeting = meetingArrayList.get(position);
        if (meeting.getmeeting_status().equalsIgnoreCase((meetingArrayList.get(position - 1).getmeeting_status()))) {
            return true;
        } else {
            return false;
        }
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
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

    @Override
    public int getItemCount() {
        return meetingArrayListFiltered.size();
    }
}