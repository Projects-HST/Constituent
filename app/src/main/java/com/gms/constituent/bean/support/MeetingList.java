package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MeetingList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("meeting_list")
    @Expose
    private ArrayList<Meeting> meetingArrayList = new ArrayList<>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The meetingArrayList
     */
    public ArrayList<Meeting> getMeetingArrayList() {
        return meetingArrayList;
    }

    /**
     * @param meetingArrayList The meetingArrayList
     */
    public void setMeetingArrayList(ArrayList<Meeting> meetingArrayList) {
        this.meetingArrayList = meetingArrayList;
    }
}