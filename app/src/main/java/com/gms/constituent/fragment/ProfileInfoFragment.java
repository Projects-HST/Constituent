package com.gms.constituent.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gms.constituent.R;
import com.gms.constituent.activity.AboutUsActivity;
import com.gms.constituent.activity.GrievanceDetailActivity;
import com.gms.constituent.activity.SelectUserActivity;
import com.gms.constituent.activity.SplashScreenActivity;
import com.gms.constituent.activity.TermsAndConditions;
import com.gms.constituent.adapter.GrievanceListAdapter;
import com.gms.constituent.bean.support.Grievance;
import com.gms.constituent.bean.support.GrievanceList;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileInfoFragment extends Fragment implements DialogClickListener, View.OnClickListener {

    private static final String TAG = NewsFragment.class.getName();
    private View rootView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private int ab = 0;
    private ArrayList<Grievance> grievances = new ArrayList<>();
    private GrievanceListAdapter grievanceListAdapter;
    private TextView address, pincode, fatherHusbandName, mobileNo, whatsappNo, emailId, religionName, aadhaarNo,
            voterIdNo, dob, gender, changeUser, aboutUs, terms, shareApp, signout;


    public static PetitionFragment newInstance(int position) {
        PetitionFragment frag = new PetitionFragment();
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

        rootView = inflater.inflate(R.layout.fragment_profile_info, container, false);

        fatherHusbandName = (TextView) rootView.findViewById(R.id.father_husband_name);
        emailId = (TextView) rootView.findViewById(R.id.email_id);
        mobileNo = (TextView) rootView.findViewById(R.id.phone_no);
        whatsappNo = (TextView) rootView.findViewById(R.id.whatsapp_no);
        dob = (TextView) rootView.findViewById(R.id.dob);
        gender = (TextView) rootView.findViewById(R.id.gender);
        religionName = (TextView) rootView.findViewById(R.id.religion);
        address = (TextView) rootView.findViewById(R.id.address);
        pincode = (TextView) rootView.findViewById(R.id.pincode);
        voterIdNo = (TextView) rootView.findViewById(R.id.voter_id);
        aadhaarNo = (TextView) rootView.findViewById(R.id.aadhaar);
        changeUser = (TextView) rootView.findViewById(R.id.change_user);
        aboutUs = (TextView) rootView.findViewById(R.id.about_us);
        terms = (TextView) rootView.findViewById(R.id.terms);
        shareApp = (TextView) rootView.findViewById(R.id.share_app);
        signout = (TextView) rootView.findViewById(R.id.sign_out);

        fatherHusbandName.setText(PreferenceStorage.getfatherORhusband(getActivity()));
        emailId.setText(PreferenceStorage.getEmail(getActivity()));
        mobileNo.setText(PreferenceStorage.getMobileNo(getActivity()));
        whatsappNo.setText(PreferenceStorage.getWhatsappNo(getActivity()));
        dob.setText(PreferenceStorage.getDob(getActivity()));
        gender.setText(PreferenceStorage.getGender(getActivity()));
        religionName.setText(PreferenceStorage.getReligionName(getActivity()));
        address.setText(PreferenceStorage.getAddress(getActivity()));
        pincode.setText(PreferenceStorage.getPincode(getActivity()));
        voterIdNo.setText(PreferenceStorage.getVoterId(getActivity()));
        aadhaarNo.setText(PreferenceStorage.getAadhaarNo(getActivity()));

        changeUser.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        terms.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        signout.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == changeUser) {
            Intent homeIntent = new Intent(getActivity(), SelectUserActivity.class);
            homeIntent.putExtra("page", "verify");
            startActivity(homeIntent);
        }if (v == aboutUs) {
            Intent homeIntent = new Intent(getActivity(), AboutUsActivity.class);
            homeIntent.putExtra("page", "verify");
            startActivity(homeIntent);
        }if (v == terms) {
            Intent homeIntent = new Intent(getActivity(), TermsAndConditions.class);
            homeIntent.putExtra("page", "verify");
            startActivity(homeIntent);
        }if (v == shareApp) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "App url after upload in playstore");
            startActivity(Intent.createChooser(i, "Share via"));
        }if (v == signout) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.sign_out));
            alertDialogBuilder.setMessage(getString(R.string.sign_out_alert));
            alertDialogBuilder.setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();


                    Intent homeIntent = new Intent(getActivity(), SplashScreenActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            });
            alertDialogBuilder.setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.show();
        }
    }


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

}
