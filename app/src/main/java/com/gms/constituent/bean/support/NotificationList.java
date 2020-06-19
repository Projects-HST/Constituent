package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("notification_list")
    @Expose
    private ArrayList<Notification> notificationArrayList = new ArrayList<>();

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
     * @return The notificationArrayList
     */
    public ArrayList<Notification> getNotificationArrayList() {
        return notificationArrayList;
    }

    /**
     * @param notificationArrayList The notificationArrayList
     */
    public void setNotificationArrayList(ArrayList<Notification> notificationArrayList) {
        this.notificationArrayList = notificationArrayList;
    }
}