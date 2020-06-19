package com.gms.constituent.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.constituent.R;
import com.gms.constituent.adapter.UserListAdapter;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.bean.support.User;
import com.gms.constituent.bean.support.UserList;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = NewsDetailActivity.class.getName();

    private News news;
    private ImageView newsImage;
    private TextView newsName, newsDate, newsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        news = (News) getIntent().getSerializableExtra("serviceObj");

        newsName = (TextView) findViewById(R.id.txt_news_name);
        newsDate = (TextView) findViewById(R.id.txt_news_date);
        newsDetail = (TextView) findViewById(R.id.txt_news_detail);
        newsImage = (ImageView) findViewById(R.id.img_logo);


        newsName.setText(news.gettitle());
        newsDate.setText(news.getnews_date());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newsDetail.setText(Html.fromHtml(news.getdetails(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            newsDetail.setText(Html.fromHtml(news.getdetails()));
        }


        if (GMSValidator.checkNullString(news.getimage_file_name())) {
            Picasso.get().load(news.getimage_file_name()).into(newsImage);
        } else {
            newsImage.setImageResource(R.drawable.news_banner);
        }

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

}
