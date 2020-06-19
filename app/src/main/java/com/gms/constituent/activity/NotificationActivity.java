package com.gms.constituent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.constituent.R;
import com.gms.constituent.adapter.NotificationListAdapter;
import com.gms.constituent.bean.support.Meeting;
import com.gms.constituent.bean.support.MeetingList;
import com.gms.constituent.bean.support.Notification;
import com.gms.constituent.bean.support.NotificationList;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = NotificationActivity.class.getName();


    private String whatRes = "";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationListAdapter notificationListAdapter;
    private RelativeLayout toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        listView = (ListView) findViewById(R.id.notification_list);
        listView.setOnItemClickListener(this);

        getNotificationList();

    }

    private void getNotificationList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
//            jsonObject.put(GMSConstants.KEY_USER_ID, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_NOTIFICATIONS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(GMSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            Gson gson = new Gson();
            NotificationList notificationList = gson.fromJson(response.toString(), NotificationList.class);
            if (notificationList.getNotificationArrayList() != null && notificationList.getNotificationArrayList().size() > 0) {
//                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getserviceArrayList());
                updateListAdapter(notificationList.getNotificationArrayList());
            } else {
                if (notifications != null) {
                    notifications.clear();
                    updateListAdapter(notificationList.getNotificationArrayList());
                }
            }
        }
    }

    protected void updateListAdapter(ArrayList<Notification> notificationArrayList) {
        notifications.clear();
        notifications.addAll(notificationArrayList);
        if (notificationListAdapter == null) {
            notificationListAdapter = new NotificationListAdapter(this, notifications);
            listView.setAdapter(notificationListAdapter);
        } else {
            notificationListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item clicked" + position);
        Notification notification = null;
        if ((notificationListAdapter != null) && (notificationListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = notificationListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            notification = notifications.get(actualindex);
        } else {
            notification = notifications.get(position);
        }
        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("serviceObj", notification);
        startActivity(intent);    }
}