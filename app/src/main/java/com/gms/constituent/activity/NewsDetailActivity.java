package com.gms.constituent.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.utils.GMSValidator;
import com.gms.constituent.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = NewsDetailActivity.class.getName();
//    private int colour = 0;
    private RelativeLayout toolbar;
    private News news;
    private ImageView newsImage;
    private TextView newsName, newsDate, newsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

//        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));

        toolbar = (RelativeLayout)findViewById(R.id.toolbar_view);
//        toolbar.setBackgroundColor(colour);

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


        newsName.setText(capitalizeString(news.gettitle()));
        newsDate.setText(news.getnews_date());
        newsDetail.setLinksClickable(true);
        newsDetail.setMovementMethod(LinkMovementMethod.getInstance());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            newsDetail.setText(HtmlCompat.fromHtml(news.getdetails(), HtmlCompat.FROM_HTML_MODE_LEGACY));
//            newsDetail.setText(SafeURLSpan.parseSafeHtml(news.getdetails()));
//            newsDetail.setMovementMethod(LinkMovementMethod.getInstance());
//        } else {
            newsDetail.setText(HtmlCompat.fromHtml(news.getdetails(), HtmlCompat.FROM_HTML_MODE_LEGACY));
//        }
//        newsDetail.setClickable(true);
//        Linkify.addLinks(newsDetail, Linkify.WEB_URLS);

        if (GMSValidator.checkNullString(news.getimage_file_name())) {
            Picasso.get().load(news.getimage_file_name()).into(newsImage);
        } else {
            newsImage.setImageResource(R.drawable.news_banner);
        }

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


