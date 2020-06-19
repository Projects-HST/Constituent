package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("father_husband_name")
    @Expose
    private String father_husband_name;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("serial_no")
    @Expose
    private String serial_no;

    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;

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
     * @return The full_name
     */
    public String getfull_name() {
        return full_name;
    }

    /**
     * @param full_name The full_name
     */
    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    /**
     * @return The father_husband_name
     */
    public String getfather_husband_name() {
        return father_husband_name;
    }

    /**
     * @param father_husband_name The father_husband_name
     */
    public void setfather_husband_name(String father_husband_name) {
        this.father_husband_name = father_husband_name;
    }

    /**
     * @return The dob
     */
    public String getdob() {
        return dob;
    }

    /**
     * @param dob The dob
     */
    public void setdob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The serial_no
     */
    public String getSerial_no() {
        return serial_no;
    }

    /**
     * @param serial_no The serial_no
     */
    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    /**
     * @return The profile_picture
     */
    public String getprofile_picture() {
        return profile_picture;
    }

    /**
     * @param profile_picture The profile_picture
     */
    public void setprofile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

}