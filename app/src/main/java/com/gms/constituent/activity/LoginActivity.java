package com.gms.constituent.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.ConstituencyList;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.FirstTimePreference;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = LoginActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private EditText edtNumber, edtReferral;
    private Button signIn;
    private TextView skip;
    private ImageView laang;
    String IMEINo = "";
    private RelativeLayout selectConstituency;
    private String whatRes = "";
    private ConstituencyList constituencyList;
    private LinearLayout layoutSpinner;
    private String feebackAns = "";
    private int pooos, constituencyCount;
    private TextView constituencyCancel, constituencyOK, contistuencyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        edtNumber = (EditText) findViewById(R.id.edittext_number);
        signIn = findViewById(R.id.btn_login);
        signIn.setOnClickListener(this);

        contistuencyText = (TextView) findViewById(R.id.text_constituency);
        selectConstituency = (RelativeLayout) findViewById(R.id.select_constituency);
        selectConstituency.setOnClickListener(this);

        constituencyCancel = (TextView) findViewById(R.id.selection_cancel);
        constituencyOK = (TextView) findViewById(R.id.selection_done);
        constituencyCancel.setOnClickListener(this);
        constituencyOK.setOnClickListener(this);

        layoutSpinner = (LinearLayout) findViewById(R.id.list_view);
        layoutSpinner.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        FirstTimePreference prefFirstTime = new FirstTimePreference(getApplicationContext());

//        ScrollView view = findViewById(R.id.scrollList);
//        view.getViewTreeObserver()
//                .addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(view));

    }

    private void getConstituencyList() {
        whatRes = "constituency";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_PARTY_ID, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.CONSTITUENCY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


    private void sendNumber() {
        whatRes = "number";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, edtNumber.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_OTP;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


    @Override
    public void onClick(View v) {
        if (v == selectConstituency) {
            getConstituencyList();
        } else if (v == constituencyCancel) {
            layoutSpinner.removeAllViews();
            findViewById(R.id.spinner_layout).setVisibility(View.GONE);
        } else if (v == constituencyOK) {
            sendSelectedConstituency();
        } else if (v == signIn) {
            if (validateFields()) {
                PreferenceStorage.saveMobileNo(this, edtNumber.getText().toString());
                sendNumber();
            }
        }
    }

    private boolean validateFields() {
        if (!GMSValidator.checkMobileNumLength(this.edtNumber.getText().toString().trim())) {
            edtNumber.setError(getString(R.string.error_number));
            requestFocus(edtNumber);
            return false;
        }
        if (!GMSValidator.checkNullString(this.edtNumber.getText().toString().trim())) {
            edtNumber.setError(getString(R.string.empty_entry));
            requestFocus(edtNumber);
            return false;
        }
        if (contistuencyText.getText().toString().equalsIgnoreCase(getString(R.string.select_constituency))) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.select_constituency_alert));
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
            try {
                if (whatRes.equalsIgnoreCase("constituency")) {
                    Gson gson = new Gson();
                    constituencyList = gson.fromJson(response.toString(), ConstituencyList.class);
                    if (constituencyList.getConstituencyArrayList() != null && constituencyList.getConstituencyArrayList().size() > 0) {
                        int totalCount = constituencyList.getCount();
//                    this.serviceHistoryArrayList.addAll(ongoingServiceList.getserviceArrayList());
                        boolean isLoadingForFirstTime = false;
//                        updateListAdapter(serviceHistoryList.getFeedbackArrayList());
                        constituencyCount = constituencyList.getConstituencyArrayList().size();
                        loadMembersList(constituencyCount);
                        findViewById(R.id.spinner_layout).setVisibility(View.VISIBLE);
                    }
                } else if (whatRes.equalsIgnoreCase("number")) {
                    Intent intent = new Intent(this, NumberVerificationActivity.class);
                    startActivity(intent);
                } else if (whatRes.equalsIgnoreCase("ANS")) {
                    String constituencyID = response.getJSONArray("data").getJSONObject(0).getString("constituency_id");
                    String constituencyName = response.getJSONArray("data").getJSONObject(0).getString("constituency_name");
                    String clientURL = response.getJSONArray("data").getJSONObject(0).getString("client_api_url");

                    PreferenceStorage.saveConstituencyID(this, constituencyID);
                    PreferenceStorage.saveConstituencyName(this, constituencyName);
                    PreferenceStorage.saveClientUrl(this, clientURL);
                    contistuencyText.setText(constituencyName);
                    layoutSpinner.removeAllViews();
                    findViewById(R.id.spinner_layout).setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    private void loadMembersList(int memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount; c1++) {
                RadioButton constiRadio = new RadioButton(this);
                RadioGroup.LayoutParams radioParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioParams.setMargins(20, 10, 10, 10);
                constiRadio.setLayoutParams(radioParams);

                String consti = constituencyList.getConstituencyArrayList().get(c1).getconstituency_name();
                constiRadio.setText(consti);
                constiRadio.setId(R.id.radio_constituency);
                constiRadio.setTextSize(18.0f);
                constiRadio.setElevation(20.0f);
                constiRadio.setPadding(10, 0, 0, 0);
                constiRadio.setTextColor(ContextCompat.getColor(this, R.color.black));
                constiRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));

                final int finalC = c1;

                constiRadio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == constiRadio) {
                            pooos = finalC;
                            onRadioButtonClicked();
                        }
                    }
                });
                if (c1 == 0) {
                    constiRadio.setChecked(true);
                } else {
                    constiRadio.setChecked(false);
                }
                layoutSpinner.addView(constiRadio);
            }
            ScrollView.LayoutParams params = null;
            if (memberCount < 5) {
                int height = memberCount * 100;
                params = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);

            } else {
                params = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            }
            layoutSpinner.setLayoutParams(params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRadioButtonClicked() {
        for (int position = 0; position < constituencyCount; position++) {
            RadioButton rad = layoutSpinner.getChildAt(position).findViewById(R.id.radio_constituency);
            if (position == pooos) {
                rad.setChecked(true);
            } else {
                rad.setChecked(false);
            }

        }
    }

    private void sendSelectedConstituency() {
        whatRes = "ANS";
        int idConsti = 0;
        for (int position = 0; position < constituencyCount; position++) {
            RadioButton rad = layoutSpinner.getChildAt(position).findViewById(R.id.radio_constituency);
            if (rad.isChecked()) {
                idConsti = position;
            }

        }

        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = constituencyList.getConstituencyArrayList().get(idConsti).getid();

        try {
            jsonObject.put(GMSConstants.KEY_JUST_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.SELECTED_CONSTITUENCY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


    private static class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static int maxHeight = 400;
        private View view;

        public OnViewGlobalLayoutListener(View view) {
            this.view = view;
        }

        @Override
        public void onGlobalLayout() {
            if (view.getHeight() > maxHeight)
                view.getLayoutParams().height = maxHeight;
        }
    }

}


