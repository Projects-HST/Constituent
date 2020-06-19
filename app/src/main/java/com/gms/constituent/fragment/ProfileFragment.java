package com.gms.constituent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gms.constituent.R;
import com.gms.constituent.activity.NotificationActivity;
import com.gms.constituent.adapter.ProfileFragmentAdapter;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    private static final String TAG = ProfileFragment.class.getName();

    private View rootView;
    private LinearLayout grievance, meeting, plantDoantion;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private int ab = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.TabLayoutOnPageChangeListener tabatab;
    private TextView userName, txtConstituencyName;
    private ImageView profilePic;

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

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        userName = (TextView) rootView.findViewById(R.id.user_name);
        txtConstituencyName = (TextView) rootView.findViewById(R.id.constituency_name);
        profilePic = (ImageView) rootView.findViewById(R.id.profile_img);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        getProfileInfo();

        return rootView;
    }

    private void getProfileInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_USER_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


    private void initialiseTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.constituency)));

        final ProfileFragmentAdapter adapter = new ProfileFragmentAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);
        tabatab = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(tabatab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }
        });
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() <= 2) {
            tabLayout.setTabMode(TabLayout.
                    MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.
                    MODE_SCROLLABLE);
        }
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
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
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
            try {
                JSONObject data = response.getJSONArray("user_details").getJSONObject(0);
                String name = data.getString("full_name");
                String address = data.getString("address");
                String pincode = data.getString("pin_code");
                String fatherHusbandName = data.getString("father_husband_name");
                String mobileNo = data.getString("mobile_no");
                String whatsappNo = data.getString("whatsapp_no");
                String emailId = data.getString("email_id");
                String religionName = data.getString("religion_name");
                String constituencyName = data.getString("constituency_name");
                String paguthiName = data.getString("paguthi_name");
                String wardName = data.getString("ward_name");
                String boothName = data.getString("booth_name");
                String boothAddress = data.getString("booth_address");
                String serialNo = data.getString("serial_no");
                String aadhaarNo = data.getString("aadhaar_no");
                String voterIdNo = data.getString("voter_id_no");
                String dob = data.getString("dob");
                String gender = data.getString("gender");
                String profilePicture = data.getString("profile_picture");
                PreferenceStorage.saveName(getActivity(), name);
                PreferenceStorage.saveAddress(getActivity(), address);
                PreferenceStorage.savePincode(getActivity(), pincode);
                PreferenceStorage.savefatherORhusband(getActivity(), fatherHusbandName);
                PreferenceStorage.saveMobileNo(getActivity(), mobileNo);
                PreferenceStorage.saveWhatsappNo(getActivity(), whatsappNo);
                PreferenceStorage.saveEmail(getActivity(), emailId);
                PreferenceStorage.saveReligionName(getActivity(), religionName);
                PreferenceStorage.saveConstituencyName(getActivity(), constituencyName);
                PreferenceStorage.savePaguthiName(getActivity(), paguthiName);
                PreferenceStorage.saveWard(getActivity(), wardName);
                PreferenceStorage.saveBooth(getActivity(), boothName);
                PreferenceStorage.saveBoothAddress(getActivity(), boothAddress);
                PreferenceStorage.saveSerialNo(getActivity(), serialNo);
                PreferenceStorage.saveAadhaarNo(getActivity(), aadhaarNo);
                PreferenceStorage.saveVoterId(getActivity(), voterIdNo);
                PreferenceStorage.saveDob(getActivity(), dob);
                PreferenceStorage.saveGender(getActivity(), gender);
                PreferenceStorage.saveProfilePic(getActivity(), profilePicture);

                if (GMSValidator.checkNullString(profilePicture)) {
                    Picasso.get().load(profilePicture).into(profilePic);
                } else {
                    profilePic.setImageResource(R.drawable.ic_profile);
                }

                userName.setText(name);
                txtConstituencyName.setText(constituencyName);

                initialiseTabs();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
    }

    @Override
    public void onClick(View v) {

    }
}
