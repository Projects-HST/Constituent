package com.gms.constituent.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

public class CustomUrlSpan extends URLSpan {
    public CustomUrlSpan(String url) {
        super(url);
    }

    /* You may want to properly support the ParcelableSpan interface as well */

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());

        // Put your custom intent handling in here

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
        }
    }
}