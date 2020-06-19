package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("news_list")
    @Expose
    private ArrayList<News> newsArrayList = new ArrayList<>();

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
     * @return The newsArrayList
     */
    public ArrayList<News> getNewsArrayList() {
        return newsArrayList;
    }

    /**
     * @param newsArrayList The newsArrayList
     */
    public void setNewsArrayList(ArrayList<News> newsArrayList) {
        this.newsArrayList = newsArrayList;
    }
}