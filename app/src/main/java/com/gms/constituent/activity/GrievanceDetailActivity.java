package com.gms.constituent.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.Grievance;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

public class GrievanceDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = GrievanceDetailActivity.class.getName();
    private int colour = 0;
    private RelativeLayout toolbar;
    private Grievance grievance;
    private TextView txtConstituency, seekerType, txtPetitionEnquiry, petitionEnquiryNo, grievanceName,
            grievanceSubCat, grievanceDesc, grievanceReference, grievanceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

//        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));

        toolbar = (RelativeLayout)findViewById(R.id.toolbar_view);
//        toolbar.setBackgroundColor(colour);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        grievance = (Grievance) getIntent().getSerializableExtra("serviceObj");

        txtConstituency = (TextView) findViewById(R.id.text_constituency);
        seekerType = (TextView) findViewById(R.id.seeker_type);
        txtPetitionEnquiry = (TextView) findViewById(R.id.txt_petition_enquiry);
        petitionEnquiryNo = (TextView) findViewById(R.id.petition_enquiry_number);
        grievanceName = (TextView) findViewById(R.id.grievance_name);
        grievanceSubCat = (TextView) findViewById(R.id.grievance_sub_category);
        grievanceDesc = (TextView) findViewById(R.id.grievance_description);
        grievanceReference = (TextView) findViewById(R.id.grievance_reference_note);
        grievanceStatus = (TextView) findViewById(R.id.grievance_status);


        txtConstituency.setText(PreferenceStorage.getConstituencyName(this));
        seekerType.setText(grievance.getseeker_info());

        if (grievance.getgrievance_type().equalsIgnoreCase("P")) {
            txtPetitionEnquiry.setText(getString(R.string.petition_num));
        } else {
            txtPetitionEnquiry.setText(getString(R.string.enquiry_num));
            findViewById(R.id.desc_layout).setVisibility(View.GONE);
        }
        petitionEnquiryNo.setText(grievance.getpetition_enquiry_no());
        grievanceName.setText(capitalizeString(grievance.getgrievance_name()));
        grievanceSubCat.setText(capitalizeString(grievance.getSub_category_name()));
        grievanceDesc.setText(capitalizeString(grievance.getdescription()));
        grievanceReference.setText(capitalizeString(grievance.getreference_note()));
        grievanceStatus.setText(capitalizeString(grievance.getstatus()));

        if (grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
            grievanceStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_completed));
        } else {
            grievanceStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_processing));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
        }

    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

}