package com.gms.constituent.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gms.constituent.R;
import com.gms.constituent.fragment.HomeFragment;
import com.gms.constituent.fragment.NewsFragment;
import com.gms.constituent.fragment.ProfileFragment;
import com.gms.constituent.fragment.SettingsFragment;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.CommonUtils;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements IServiceListener, DialogClickListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    int checkPointSearch = 0;
    boolean doubleBackToExitPressedOnce = false;
    private View notificationBadge;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));

        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        toolbar.setTitle(getString(R.string.home_title));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        setSupportActionBar(toolbar);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        callGetSubCategoryService();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.navigation_home:
//                                item.setIcon(R.drawable.ic_home_selected);
                                changeFragment(0);
//                                notificationBadge.setVisibility(View.VISIBLE);
                                break;
                            case R.id.navigation_news:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    item.setIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bottom_nav_item_color));
                                }
                                changeFragment(1);
//                                notificationBadge.setVisibility(View.VISIBLE);
                                break;
                            case R.id.navigation_profile:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    item.setIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bottom_nav_item_color));
                                }
                                changeFragment(2);
//                                notificationBadge.setVisibility(View.VISIBLE);
                                break;
                            case R.id.navigation_settings:
//                                item.setIcon(R.drawable.ic_settings_selected);
                                changeFragment(3);
//                                notificationBadge.setVisibility(View.VISIBLE);
                                break;
                        }
                        return true;
                    }
                });

        changeFragment(0);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
//        addBadgeView();
    }

//    private void addBadgeView() {
//            removeBadge(bottomNavigationView, 0);
//            BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
//            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0); //set this to 0, 1, 2, or 3.. accordingly which menu item of the bottom bar you want to show badge
//            notificationBadge = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottom_menu_indicator, menuView, false);
//            itemView.addView(notificationBadge);
//    }
//    public void removeBadge(BottomNavigationView navigationView, int index) {
//        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
//        View v = bottomNavigationMenuView.getChildAt(index);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
//        itemView.removeViewAt(itemView.getChildCount() - 1);
//    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
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

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            toolbar.setTitle(getString(R.string.home_title));
            checkPointSearch = 0;
            newFragment = new HomeFragment();
        } else if (position == 1) {
            checkPointSearch = 1;
            toolbar.setTitle(getString(R.string.menu_news));
            newFragment = new NewsFragment();
        } else if (position == 2) {
            checkPointSearch = 2;
            toolbar.setTitle(getString(R.string.profile_title));
            newFragment = new ProfileFragment();
        } else if (position == 3) {
            checkPointSearch = 3;
            toolbar.setTitle(getString(R.string.profile_title));
            newFragment = new SettingsFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    public void callGetSubCategoryService() {
        if (CommonUtils.isNetworkAvailable(this)) {
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            checkVersion();
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private void checkVersion() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        try {
            jsonObject.put(GMSConstants.KEY_APP_VERSION, GMSConstants.KEY_APP_VERSION_VALUE);
//            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.CHECK_VERSION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        try {
            String status = response.getString("status");
            if (!status.equalsIgnoreCase("success")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Update");
                alertDialogBuilder.setMessage("A new version of GMS is available!");
                alertDialogBuilder.setPositiveButton("Get it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            finish();
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}
