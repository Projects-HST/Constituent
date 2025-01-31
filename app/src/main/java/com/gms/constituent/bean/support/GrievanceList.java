package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GrievanceList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("grievance_list")
    @Expose
    private ArrayList<Grievance> grievanceArrayList = new ArrayList<>();

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
     * @return The grievanceArrayList
     */
    public ArrayList<Grievance> getGrievanceArrayList() {
        return grievanceArrayList;
    }

    /**
     * @param grievanceArrayList The grievanceArrayList
     */
    public void setGrievanceArrayList(ArrayList<Grievance> grievanceArrayList) {
        this.grievanceArrayList = grievanceArrayList;
    }
}