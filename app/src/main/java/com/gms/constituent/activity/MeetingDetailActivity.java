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
import com.gms.constituent.bean.support.Meeting;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

public class MeetingDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = MeetingDetailActivity.class.getName();
    //    private int colour = 0;
    private RelativeLayout toolbar;
    private Meeting meeting;
    private ImageView newsImage;
    private TextView meetingTitle, meetingDetail, requestDate, meetingDate, meetingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

//        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));

        toolbar = (RelativeLayout) findViewById(R.id.toolbar_view);
//        toolbar.setBackgroundColor(colour);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        meeting = (Meeting) getIntent().getSerializableExtra("serviceObj");

        meetingTitle = (TextView) findViewById(R.id.meeting_title);
        meetingDetail = (TextView) findViewById(R.id.meeting_details);
        requestDate = (TextView) findViewById(R.id.created_on);
        meetingStatus = (TextView) findViewById(R.id.meet_status);
        meetingDate = (TextView) findViewById(R.id.schedule_date);

        meetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        meetingDetail.setText(capitalizeString(meeting.getmeeting_detail()));
        requestDate.setText(meeting.getcreated_at());
        meetingStatus.setText(capitalizeString(meeting.getmeeting_status()));

        if (meeting.getmeeting_status().equalsIgnoreCase("REQUESTED")) {
            meetingDate.setVisibility(View.GONE);
            meetingStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.requested));
        } else {
            if (meeting.getmeeting_status().equalsIgnoreCase("SCHEDULED")) {
                meetingDate.setVisibility(View.VISIBLE);
                meetingDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
                meetingStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.scheduled));
            } else {
                meetingDate.setVisibility(View.VISIBLE);
                meetingDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
                meetingStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.completed_meeting));
            }
        }
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
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