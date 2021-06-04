package com.gms.constituent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gms.constituent.R;
import com.gms.constituent.bean.database.SQLiteHelper;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    //    AppSignatureHelper appSignatureHelper;
    SQLiteHelper database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        database = new SQLiteHelper(getApplicationContext());
//        appSignatureHelper = new AppSignatureHelper(this);
//        appSignatureHelper.getAppSignatures();
        final int getStatus = database.appInfoCheck();

        /*ArrayList<String> appCodes = new ArrayList<>();
        SmsVerification hash = new SmsVerification(getBaseContext());
        appCodes= hash.getAppSignatures();
        String yourhash = appCodes.get(0);
        Log.d("Hash Key: ", yourhash);*/

//        Toast.makeText(SplashScreenActivity.this, "Hash key...  " + yourhash , Toast.LENGTH_SHORT).show();
//        hashFromSHA1("60:53:85:D2:89:8C:8A:AA:8B:55:D1:DE:73:A9:2D:C1:97:C7:27:A7");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean b = PreferenceStorage.isFirstTimeLaunch(getApplicationContext());
                String id = PreferenceStorage.getUserId(getApplicationContext());
                if (getStatus !=0 && GMSValidator.checkNullString(id)) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    boolean bb = PreferenceStorage.getSelectUserPage(getApplicationContext());
                    if (bb) {
                        Intent homeIntent = new Intent(getApplicationContext(), SelectUserActivity.class);
                        homeIntent.putExtra("page", "verify");
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    homeIntent.putExtra("profile_state", "new");
                        startActivity(homeIntent);
                        finish();
                    }
                    else if (getStatus != 0) {
                        Intent i = new Intent(SplashScreenActivity.this, ConstituencyIdActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();
                                Log.e("newToken", newToken);
                                PreferenceStorage.saveGCM(getApplicationContext(), newToken);
                            }
                        });
                        startActivity(i);
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new byte[arr.length];

        for (int i = 0; i < arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hashfaka : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }

}

