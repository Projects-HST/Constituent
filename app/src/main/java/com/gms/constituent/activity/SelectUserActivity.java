package com.gms.constituent.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gms.constituent.R;
import com.gms.constituent.adapter.UserListAdapter;
import com.gms.constituent.bean.support.User;
import com.gms.constituent.bean.support.UserList;
import com.gms.constituent.customview.CustomOtpEditText;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.CommonUtils;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectUserActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = SelectUserActivity.class.getName();


    private String whatRes = "";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private ArrayList<User> usersArray = new ArrayList<>();
    private UserListAdapter userListAdapter;
    private RelativeLayout toolBar;
    String page = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        listView = (ListView) findViewById(R.id.user_list);
        listView.setOnItemClickListener(this);

        page = getIntent().getStringExtra("page");

        if (page.equalsIgnoreCase("verify")) {

//            toolBar = (RelativeLayout) findViewById(R.id.toolbar_view);
//            toolBar.setVisibility(View.GONE);
            findViewById(R.id.img_back).setVisibility(View.GONE);

        }

        getUserList();

    }

    private void getUserList() {
        whatRes = "user_list";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, PreferenceStorage.getMobileNo(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_USER_LIST;
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
            UserList userList = gson.fromJson(response.toString(), UserList.class);
            if (userList.getUserArrayList() != null && userList.getUserArrayList().size() > 0) {
//                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getserviceArrayList());
                updateListAdapter(userList.getUserArrayList());
            } else {
                if (usersArray != null) {
                    usersArray.clear();
                    updateListAdapter(userList.getUserArrayList());
                }
            }
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
        PreferenceStorage.saveUserId(this, user.getid());
        PreferenceStorage.saveName(this, user.getfull_name());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("serviceObj", user);
        startActivity(intent);
        finish();
    }
}
