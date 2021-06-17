package com.gms.constituent.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gms.constituent.R;
import com.gms.constituent.activity.AboutUsActivity;
import com.gms.constituent.activity.SelectUserActivity;
import com.gms.constituent.activity.SplashScreenActivity;
import com.gms.constituent.activity.TermsAndConditions;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = NewsFragment.class.getName();
    private ImageView changeUser, aboutUs, terms, privacy, help;
    private TextView signOut;
    private View rootView;

    public static SettingsFragment newInstance(int position) {
        SettingsFragment frag = new SettingsFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        changeUser = (ImageView) rootView.findViewById(R.id.userClick);
        aboutUs = (ImageView) rootView.findViewById(R.id.abtClick);
        terms = (ImageView) rootView.findViewById(R.id.termsClick);
        privacy = (ImageView) rootView.findViewById(R.id.privacyClick);
        help = (ImageView) rootView.findViewById(R.id.helpClick);
        signOut = (TextView) rootView.findViewById(R.id.logout);

        changeUser.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        terms.setOnClickListener(this);
        privacy.setOnClickListener(this);
        help.setOnClickListener(this);
        signOut.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == changeUser) {
            Intent homeIntent = new Intent(getActivity(), SelectUserActivity.class);
            homeIntent.putExtra("page", "profile");
            startActivity(homeIntent);
        }if (v == aboutUs) {
            Intent homeIntent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(homeIntent);
        }if (v == terms) {
            Intent homeIntent = new Intent(getActivity(), TermsAndConditions.class);
            startActivity(homeIntent);
        }if (v == privacy) {
//            Intent i = new Intent(android.content.Intent.ACTION_SEND);
//            i.setType("text/plain");
//            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
//            i.putExtra(android.content.Intent.EXTRA_TEXT, "App url after upload in playstore");
//            startActivity(Intent.createChooser(i, "Share via"));
        }if (v == help) {
//            Intent i = new Intent(android.content.Intent.ACTION_SEND);
//            i.setType("text/plain");
//            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
//            i.putExtra(android.content.Intent.EXTRA_TEXT, "App url after upload in playstore");
//            startActivity(Intent.createChooser(i, "Share via"));
        }
        if (v == signOut) {
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
}