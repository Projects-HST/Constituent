package com.gms.constituent.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.bean.support.Notification;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.utils.GMSValidator;
import com.squareup.picasso.Picasso;

public class NotificationDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = NotificationDetailActivity.class.getName();

    private Notification notification;
    public TextView txtNotificationDate, txtNotificationTime, txtNotificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        notification = (Notification) getIntent().getSerializableExtra("serviceObj");

        txtNotificationDate = (TextView) findViewById(R.id.notification_date);
        txtNotificationTime = (TextView) findViewById(R.id.notification_time);
        txtNotificationText = (TextView) findViewById(R.id.notification_detail);


        txtNotificationDate.setText(notification.getcreated_at());
        txtNotificationTime.setText(notification.getcreated_time());
        txtNotificationText.setText(capitalizeString(notification.getnotification_text()));


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