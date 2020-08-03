package com.gms.constituent.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.Meeting;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.utils.GMSValidator;
import com.squareup.picasso.Picasso;

public class MeetingDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = MeetingDetailActivity.class.getName();

    private Meeting meeting;
    private ImageView newsImage;
    private TextView meetingTitle, meetingDetail, meetingDate, meetingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        meeting = (Meeting) getIntent().getSerializableExtra("serviceObj");

        meetingTitle = (TextView) findViewById(R.id.meeting_title);
        meetingDetail = (TextView) findViewById(R.id.meeting_details);
        meetingDate = (TextView) findViewById(R.id.meeting_date);
        meetingStatus = (TextView) findViewById(R.id.meeting_status);


        meetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        meetingDetail.setText(capitalizeString(meeting.getmeeting_detail()));
        meetingDate.setText(meeting.getmeeting_date());
        meetingStatus.setText(capitalizeString(meeting.getmeeting_status()));
        if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")) {
            meetingStatus.setTextColor(ContextCompat.getColor(this, R.color.completed_meeting));
        } else {
            meetingStatus.setTextColor(ContextCompat.getColor(this, R.color.requested));
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