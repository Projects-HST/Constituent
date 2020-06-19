package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("news_date")
    @Expose
    private String news_date;

    @SerializedName("image_file_name")
    @Expose
    private String image_file_name;

    @SerializedName("title")
    @Expose
    private String title;
    
    @SerializedName("details")
    @Expose
    private String details;

    /**
     * @return The id
     */
    public String getid() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setid(String id) {
        this.id = id;
    }

    /**
     * @return The news_date
     */
    public String getnews_date() {
        return news_date;
    }

    /**
     * @param news_date The news_date
     */
    public void setnews_date(String news_date) {
        this.news_date = news_date;
    }

    /**
     * @return The image_file_name
     */
    public String getimage_file_name() {
        return image_file_name;
    }

    /**
     * @param image_file_name The image_file_name
     */
    public void setimage_file_name(String image_file_name) {
        this.image_file_name = image_file_name;
    }

    /**
     * @return The title
     */
    public String gettitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void settitle(String title) {
        this.title = title;
    }

    /**
     * @return The details
     */
    public String getdetails() {
        return details;
    }

    /**
     * @param details The details
     */
    public void setdetails(String details) {
        this.details = details;
    }

}