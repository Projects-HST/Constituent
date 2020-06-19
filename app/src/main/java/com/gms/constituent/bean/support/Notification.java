package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notification implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("constituent_id")
    @Expose
    private String constituent_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("created_time")
    @Expose
    private String created_time;

    @SerializedName("notification_text")
    @Expose
    private String notification_text;

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
     * @return The constituent_id
     */
    public String getconstituent_id() {
        return constituent_id;
    }

    /**
     * @param constituent_id The constituent_id
     */
    public void setconstituent_id(String constituent_id) {
        this.constituent_id = constituent_id;
    }

    /**
     * @return The created_at
     */
    public String getcreated_at() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public void setcreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return The created_time
     */
    public String getcreated_time() {
        return created_time;
    }

    /**
     * @param created_time The created_time
     */
    public void setcreated_time(String created_time) {
        this.created_time = created_time;
    }

    /**
     * @return The notification_text
     */
    public String getnotification_text() {
        return notification_text;
    }

    /**
     * @param notification_text The notification_text
     */
    public void setnotification_text(String notification_text) {
        this.notification_text = notification_text;
    }


}