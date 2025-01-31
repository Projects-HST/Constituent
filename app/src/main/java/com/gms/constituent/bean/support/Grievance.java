package com.gms.constituent.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Grievance implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("seeker_info")
    @Expose
    private String seeker_info;

    @SerializedName("grievance_name")
    @Expose
    private String grievance_name;

    @SerializedName("grievance_type")
    @Expose
    private String grievance_type;

    @SerializedName("petition_enquiry_no")
    @Expose
    private String petition_enquiry_no;

    @SerializedName("grievance_date")
    @Expose
    private String grievance_date;

    @SerializedName("sub_category_name")
    @Expose
    private String sub_category_name;

    @SerializedName("reference_note")
    @Expose
    private String reference_note;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("status")
    @Expose
    private String status;

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
     * @return The seeker_info
     */
    public String getseeker_info() {
        return seeker_info;
    }

    /**
     * @param seeker_info The seeker_info
     */
    public void setseeker_info(String seeker_info) {
        this.seeker_info = seeker_info;
    }

    /**
     * @return The grievance_name
     */
    public String getgrievance_name() {
        return grievance_name;
    }

    /**
     * @param grievance_name The grievance_name
     */
    public void setgrievance_name(String grievance_name) {
        this.grievance_name = grievance_name;
    }

    /**
     * @return The grievance_type
     */
    public String getgrievance_type() {
        return grievance_type;
    }

    /**
     * @param grievance_type The grievance_type
     */
    public void setgrievance_type(String grievance_type) {
        this.grievance_type = grievance_type;
    }

    /**
     * @return The petition_enquiry_no
     */
    public String getpetition_enquiry_no() {
        return petition_enquiry_no;
    }

    /**
     * @param petition_enquiry_no The petition_enquiry_no
     */
    public void setpetition_enquiry_no(String petition_enquiry_no) {
        this.petition_enquiry_no = petition_enquiry_no;
    }

    /**
     * @return The grievance_date
     */
    public String getgrievance_date() {
        return grievance_date;
    }

    /**
     * @param grievance_date The grievance_date
     */
    public void setgrievance_date(String grievance_date) {
        this.grievance_date = grievance_date;
    }

    /**
     * @return The reference_note
     */
    public String getreference_note() {
        return reference_note;
    }

    /**
     * @param reference_note The reference_note
     */
    public void setreference_note(String reference_note) {
        this.reference_note = reference_note;
    }

    /**
     * @return The description
     */
    public String getdescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setdescription(String description) {
        this.description = description;
    }

    /**
     * @return The status
     */
    public String getstatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setstatus(String status) {
        this.status = status;
    }

    /**
     * @return The sub_category_name
     */
    public String getSub_category_name() {
        return sub_category_name;
    }

    /**
     * @param sub_category_name The sub_category_name
     */
    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }


}