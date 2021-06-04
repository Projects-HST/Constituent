package com.gms.constituent.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gms.constituent.R;
import com.gms.constituent.adapter.GrievanceFragmentAdapter;
import com.gms.constituent.adapter.MeetingListAdapter;
import com.gms.constituent.adapter.UserListAdapter;
import com.gms.constituent.bean.support.Meeting;
import com.gms.constituent.bean.support.MeetingList;
import com.gms.constituent.bean.support.User;
import com.gms.constituent.bean.support.UserList;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeetingActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, MeetingListAdapter.OnItemClickListener {

    private static final String TAG = MeetingActivity.class.getName();

//    private int colour = 0;
    private RelativeLayout toolbar;
    private String whatRes = "";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private MeetingListAdapter meetingListAdapter;
    private RelativeLayout toolBar;
    private LinearLayout meetingLayout;
    private RecyclerView recyclerView;
    private TextView request, schedule, complete;
    int colour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);

        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));

        toolbar = (RelativeLayout)findViewById(R.id.toolbar_view);
//        toolbar.setBackgroundColor(colour);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        meetingLayout = (LinearLayout)findViewById(R.id.meeting_layout);
        meetingLayout.setBackgroundColor(colour);

        request = (TextView)findViewById(R.id.request);
        request.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));
        schedule = (TextView)findViewById(R.id.schedule);
        complete = (TextView)findViewById(R.id.completed);
        recyclerView = findViewById(R.id.recycler_view);

        request.setOnClickListener(this);
        schedule.setOnClickListener(this);
        complete.setOnClickListener(this);

        getMeetingList();

    }

    private void getMeetingList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));
//            jsonObject.put(GMSConstants.KEY_USER_ID, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.GET_MEETINGS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {

        if (v == request){
            request.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));
            schedule.setBackground(null);
            complete.setBackground(null);
        }else if (v == schedule){
            request.setBackground(null);
            schedule.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));
            complete.setBackground(null);
        }else if (v == complete){
            complete.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));
            request.setBackground(null);
            schedule.setBackground(null);
        }

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
            MeetingList meetingList = gson.fromJson(response.toString(), MeetingList.class);
            meetings.addAll(meetingList.getMeetingArrayList());
            MeetingListAdapter mAdapter = new MeetingListAdapter(meetings, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onItemClick(View view, int position) {
        Meeting meeting = meetings.get(position);
        Intent i = new Intent (this, MeetingDetailActivity.class);
        i.putExtra("serviceObj", meeting);
        startActivity(i);
    }
}