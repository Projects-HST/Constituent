package com.gms.constituent.adapter;

import android.app.Activity;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Meeting> meetingArrayList;
    private ArrayList<Meeting> og;
    private ArrayList<Meeting> meetingArrayListFiltered;
    Context context;
    private OnItemClickListener onItemClickListener;


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
        public TextView txtMeetingTitle, txtStatusTitle, txtMeetingDate, txtMeetingStatus;
        private ImageView meetingImage, meetingDateImage;
        private LinearLayout disableLayout;
        private FrameLayout meeting;

        public MyViewHolder(View view) {
            super(view);
            disableLayout = (LinearLayout) view.findViewById(R.id.complete_layout);
            meeting = (FrameLayout) view.findViewById(R.id.meeting_lay_click);
            meeting.setOnClickListener(this);
            txtMeetingTitle = (TextView) view.findViewById(R.id.meeting_title);
            txtStatusTitle = (TextView) view.findViewById(R.id.status_title);
            txtMeetingDate = (TextView) view.findViewById(R.id.meeting_date);
            txtMeetingStatus = (TextView) view.findViewById(R.id.meeting_status);
            meetingImage = (ImageView) view.findViewById(R.id.meeting_img);
            meetingDateImage = (ImageView) view.findViewById(R.id.meeting_date_img);


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


    public MeetingListAdapter(ArrayList<Meeting> meetingArrayList, MeetingListAdapter.OnItemClickListener onItemClickListener) {
        Collections.reverse(meetingArrayList);
        this.meetingArrayList = meetingArrayList;
        this.meetingArrayListFiltered = meetingArrayList;
        this.og = meetingArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public MeetingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_meeting, parent, false);

        return new MeetingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingListAdapter.MyViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);
        holder.txtMeetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        holder.txtMeetingDate.setText(meeting.getmeeting_date());
        holder.txtMeetingStatus.setText(capitalizeString(meeting.getmeeting_status()));

        if (meeting.getmeeting_status().equalsIgnoreCase("REQUESTED")) {
            holder.txtStatusTitle.setText("Upcoming");
            holder.txtMeetingTitle.setTextColor(ContextCompat.getColor(holder.txtMeetingStatus.getContext(), R.color.black));
            holder.txtMeetingStatus.setBackgroundColor(ContextCompat.getColor(holder.txtMeetingStatus.getContext(), R.color.requested));
            holder.meetingImage.setImageResource(R.drawable.ic_meeting_active);
            holder.meetingDateImage.setImageResource(R.drawable.ic_date_active);
            holder.disableLayout.setVisibility(View.GONE);
        } else if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")) {
            holder.txtStatusTitle.setText("Earlier");
            holder.txtMeetingTitle.setTextColor(ContextCompat.getColor(holder.txtMeetingStatus.getContext(), R.color.text_grey));
            holder.txtMeetingStatus.setBackgroundColor(ContextCompat.getColor(holder.txtMeetingStatus.getContext(), R.color.completed_meeting));
            holder.meetingImage.setImageResource(R.drawable.ic_meeting_inactive);
            holder.meetingDateImage.setImageResource(R.drawable.ic_date_inactive);
            holder.disableLayout.setVisibility(View.VISIBLE);
        }
        if (position != 0) {
            if (checkdatapos(position)) {
                holder.txtStatusTitle.setVisibility(View.GONE);
            } else {
                holder.txtStatusTitle.setVisibility(View.VISIBLE);
            }
        } else {
            holder.txtStatusTitle.setVisibility(View.VISIBLE);
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