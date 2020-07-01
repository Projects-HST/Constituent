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


        meetingTitle.setText(meeting.getmeeting_title());
        meetingDetail.setText(meeting.getmeeting_detail());
        meetingDate.setText(meeting.getmeeting_date());
        meetingStatus.setText(meeting.getmeeting_status());
        if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")) {
            meetingStatus.setTextColor(ContextCompat.getColor(this, R.color.completed));
        } else {
            meetingStatus.setTextColor(ContextCompat.getColor(this, R.color.requested));
        }


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