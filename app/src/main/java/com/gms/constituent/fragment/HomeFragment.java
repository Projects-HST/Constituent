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

    private View rootView;
    private TextView constituent;
    private LinearLayout grievance, meeting, plantDonation;
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

        constituent = rootView.findViewById(R.id.constituent);
        constituent.setText(getString(R.string.hi) + " " + PreferenceStorage.getName(getContext()) + ",");
        grievance = rootView.findViewById(R.id.grievance_layout);
        grievance.setOnClickListener(this);
        meeting = rootView.findViewById(R.id.meeting_layout);
        meeting.setOnClickListener(this);
//        plantDonation = rootView.findViewById(R.id.plant_donation_layout);
//        plantDonation.setOnClickListener(this);

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


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
