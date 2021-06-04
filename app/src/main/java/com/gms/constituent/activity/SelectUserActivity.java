package com.gms.constituent.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gms.constituent.R;
import com.gms.constituent.adapter.UserListAdapter;
import com.gms.constituent.bean.support.User;
import com.gms.constituent.bean.support.UserList;
import com.gms.constituent.customview.CircleImageView;
import com.gms.constituent.customview.CustomOtpEditText;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.CommonUtils;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectUserActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = SelectUserActivity.class.getName();
    boolean doubleBackToExitPressedOnce = false;

    private String whatRes = "";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private ArrayList<User> usersArray = new ArrayList<>();
    private UserListAdapter userListAdapter;
    private RelativeLayout toolBar;
    String page = "";
    UserList userList;
    LinearLayout userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        PreferenceStorage.saveSelectUserPage(this, true);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        listView = (ListView) findViewById(R.id.user_list);
//        listView.setOnItemClickListener(this);

        userListView = findViewById(R.id.list_view_user);

        page = getIntent().getStringExtra("page");

        if (page.equalsIgnoreCase("verify")) {
//            colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));
//            toolBar = (RelativeLayout) findViewById(R.id.toolbar_view);
//            toolBar.setBackgroundColor(colour);
            findViewById(R.id.img_back).setVisibility(View.GONE);
            TextView titel = findViewById(R.id.tvtitletext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 0, 0, 0);
            titel.setPadding(20, 0, 0, 0);
            titel.setLayoutParams(params);
        }
        getUserList();
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click BACK again to exit. If you exit without selecting user you will have to verify your number once more.", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    private void getUserList() {
        whatRes = "user_list";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, PreferenceStorage.getMobileNo(this));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.GET_USER_LIST;
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
            userList = gson.fromJson(response.toString(), UserList.class);
            loadMembersList(userList.getUserArrayList().size());
//            if (userList.getUserArrayList() != null && userList.getUserArrayList().size() > 0) {
////                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getserviceArrayList());
//                updateListAdapter(userList.getUserArrayList());
//            } else {
//                if (usersArray != null) {
//                    usersArray.clear();
//                    updateListAdapter(userList.getUserArrayList());
//                }
//            }
        }
    }

    protected void updateListAdapter(ArrayList<User> userArrayList) {
        usersArray.clear();
        usersArray.addAll(userArrayList);
        if (userListAdapter == null) {
            userListAdapter = new UserListAdapter(this, usersArray);
            listView.setAdapter(userListAdapter);
        } else {
            userListAdapter.notifyDataSetChanged();
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
        User user = null;
        if ((userListAdapter != null) && (userListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = userListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            user = usersArray.get(actualindex);
        } else {
            user = usersArray.get(position);
        }
        PreferenceStorage.saveSelectUserPage(this, false);
        PreferenceStorage.saveUserId(this, user.getid());
        PreferenceStorage.saveName(this, user.getfull_name());
//        PreferenceStorage.saveSelectUserPage(this, false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("serviceObj", user);
        startActivity(intent);
        finish();
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

    private void loadMembersList(int memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount; c1++) {

                RelativeLayout.LayoutParams cellParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cellParams.setMargins(10, 10, 10, 10);

                RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(150, 150);
                paramsImageView.addRule(RelativeLayout.CENTER_VERTICAL);

                RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsLinearLayout.setMargins(20, 0, 0, 0);
                paramsLinearLayout.addRule(RelativeLayout.RIGHT_OF, R.id.user_profile_img);

                RelativeLayout.LayoutParams paramsTextViewUserName = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsTextViewUserName.setMargins(0, 0, 0, 0);

                LinearLayout mainLayout = new LinearLayout(this);
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                mainLayout.setPadding(10,20,10,20);
                mainLayout.setClipToPadding(false);

                RelativeLayout cell = new RelativeLayout(this);
                cell.setPadding(20, 20, 20, 20);
                cell.setLayoutParams(cellParams);
                cell.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));
                cell.setElevation(10.0f);

                CircleImageView userImage = new CircleImageView(this);
                userImage.setId(R.id.user_profile_img);
                userImage.setLayoutParams(paramsImageView);

                if (GMSValidator.checkNullString(userList.getUserArrayList().get(c1).getprofile_picture())) {
                    Picasso.get().load(userList.getUserArrayList().get(c1).getprofile_picture()).into(userImage);
                } else {
                    userImage.setImageResource(R.drawable.ic_profile);
                }

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(paramsLinearLayout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);


                TextView txtUserName = new TextView(this);
                txtUserName.setLayoutParams(paramsTextViewUserName);
                txtUserName.setText(capitalizeString(userList.getUserArrayList().get(c1).getfull_name()));
                txtUserName.setId(R.id.user_name);
                txtUserName.requestFocusFromTouch();
                txtUserName.setTextSize(16.0f);
                txtUserName.setTypeface(txtUserName.getTypeface(), Typeface.BOLD);
                txtUserName.setTextColor(ContextCompat.getColor(this, R.color.black));


                TextView txtSerialNo = new TextView(this);
                txtSerialNo.setId(R.id.serial_no);
                txtSerialNo.setLayoutParams(paramsTextViewUserName);
                txtSerialNo.setTextSize(16.0f);
                txtSerialNo.setTextColor(ContextCompat.getColor(this, R.color.text_grey));
                txtSerialNo.setText("Serial No - " +userList.getUserArrayList().get(c1).getSerial_no());

                TextView txtDOB = new TextView(this);
                txtDOB.setId(R.id.serial_no);
                txtDOB.setLayoutParams(paramsTextViewUserName);
                txtDOB.setTextSize(16.0f);
                txtDOB.setTextColor(ContextCompat.getColor(this, R.color.text_grey));
                txtDOB.setText("Date of Birth - " + userList.getUserArrayList().get(c1).getdob());

                cell.addView(userImage);

                linearLayout.addView(txtUserName);
                linearLayout.addView(txtSerialNo);
                linearLayout.addView(txtDOB);

                cell.addView(linearLayout);

                int finalC = c1;
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user = null;

                        user = userList.getUserArrayList().get(finalC);

                        PreferenceStorage.saveSelectUserPage(getApplicationContext(), false);
                        PreferenceStorage.saveUserId(getApplicationContext(), user.getid());
                        PreferenceStorage.saveName(getApplicationContext(), capitalizeString(user.getfull_name()));
//        PreferenceStorage.saveSelectUserPage(this, false);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("serviceObj", user);
                        startActivity(intent);
                        finish();

                    }
                });
                mainLayout.addView(cell);
                userListView.addView(mainLayout);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
