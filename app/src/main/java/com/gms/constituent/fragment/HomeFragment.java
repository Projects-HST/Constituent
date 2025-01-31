package com.gms.constituent.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gms.constituent.R;
import com.gms.constituent.activity.GrievanceActivity;
import com.gms.constituent.activity.MeetingActivity;
import com.gms.constituent.activity.NotificationActivity;
import com.gms.constituent.activity.PlantDonationActivity;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class HomeFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    private static final String TAG = ProfileFragment.class.getName();

    private View rootView;
    private TextView constituent, grievancesCount, meetingsCount;
    private LinearLayout grievance, meeting;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private int ab = 0;

    public static ProfileFragment newInstance(int position) {
        ProfileFragment frag = new ProfileFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        constituent = rootView.findViewById(R.id.constituent);
        constituent.setText((getString(R.string.hi) + ", " + PreferenceStorage.getName(getContext())));
        grievancesCount = rootView.findViewById(R.id.grievance_count);
        grievance = rootView.findViewById(R.id.grievance_layout);
        grievance.setOnClickListener(this);
        meetingsCount = rootView.findViewById(R.id.meeting_count);
        meeting = rootView.findViewById(R.id.meeting_layout);
        meeting.setOnClickListener(this);
//        plantDonation = rootView.findViewById(R.id.plant_donation_layout);
//        plantDonation.setOnClickListener(this);
        getUserInfo();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.right_menu, menu);
        
        if (ab != 0) {
//            menu.getItem(2).setIcon(ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_notification_red));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Workaround for SearchView close listener
        if (id == R.id.menu_item) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == grievance) {
            Intent intent = new Intent(getActivity(), GrievanceActivity.class);
//            intent.putExtra("cat", category);
            startActivity(intent);
        }
        if (v == meeting) {
            Intent intent = new Intent(getActivity(), MeetingActivity.class);
//            intent.putExtra("cat", category);
            startActivity(intent);
        }
//        if (v == plantDonation) {
//            Intent intent = new Intent(getActivity(), PlantDonationActivity.class);
////            intent.putExtra("cat", category);
//            startActivity(intent);
//        }

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
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    public void getUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(getActivity()));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.GET_USER_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getNewsBanner() {
//        checkRes = "banner";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(getActivity()));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.GET_NEWS_BANNER;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                JSONObject data = response.getJSONArray("user_details").getJSONObject(0);
                String totalGrievances = data.getString("grievance_count");
                String totalMeetings = data.getString("meeting_count");

                grievancesCount.setText(totalGrievances);
                meetingsCount.setText(totalMeetings);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }
}
