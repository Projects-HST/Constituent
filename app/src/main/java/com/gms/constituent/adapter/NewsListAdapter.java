package com.gms.constituent.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gms.constituent.R;
import com.gms.constituent.bean.support.News;
import com.gms.constituent.utils.GMSValidator;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NewsListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<News> news;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();


    public NewsListAdapter(Context context, ArrayList<News> news) {
        this.context = context;
        this.news = news;
//        Collections.reverse(services);
//        transformation = new RoundedTransformationBuilder()
//                .cornerRadiusDp(0)
//                .oval(false)
//                .build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if (mSearching) {
            if (!mAnimateSearch) {
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();
        } else {
            return news.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return news.get(mValidSearchIndices.get(position));
        } else {
            return news.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NewsListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_news, parent, false);

            holder = new NewsListAdapter.ViewHolder();
            holder.txtNewsTitle = (TextView) convertView.findViewById(R.id.txt_news_name);
            holder.txtDaysAgo = (TextView) convertView.findViewById(R.id.txt_news_date);
            holder.newsImage = (ImageView) convertView.findViewById(R.id.img_logo);
            convertView.setTag(holder);
        } else {
            holder = (NewsListAdapter.ViewHolder) convertView.getTag();
            holder.txtNewsTitle = (TextView) convertView.findViewById(R.id.txt_news_name);
            holder.txtDaysAgo = (TextView) convertView.findViewById(R.id.txt_news_date);
            holder.newsImage = (ImageView) convertView.findViewById(R.id.img_logo);

        }
        Long dateDiff = getDateDiff(new SimpleDateFormat("dd-MM-yyyy"), news.get(position).getnews_date());
        holder.txtNewsTitle.setText(news.get(position).gettitle());
        holder.txtNewsTitle.setEllipsize(TextUtils.TruncateAt.END);

        if (dateDiff == 0) {
            holder.txtDaysAgo.setText("Today");
        } else {
            holder.txtDaysAgo.setText(dateDiff + " days ago");
        }
        holder.txtDaysAgo.setCompoundDrawables(ContextCompat.getDrawable(context, R.drawable.ic_clock), null, null, null);
        if (GMSValidator.checkNullString(news.get(position).getimage_file_name())) {
            Picasso.get().load(news.get(position).getimage_file_name()).into(holder.newsImage);
        } else {
            holder.newsImage.setImageResource(R.drawable.news_banner);
        }

        if (mSearching) {
            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < news.size(); i++) {
            String homeWorkTitle = news.get(i).getid();
            if ((homeWorkTitle != null) && !(homeWorkTitle.isEmpty())) {
                if (homeWorkTitle.toLowerCase().contains(eventName.toLowerCase())) {
                    mValidSearchIndices.add(i);
                }
            }
        }
        Log.d("Event List Adapter", "notify" + mValidSearchIndices.size());
    }

    public void exitSearch() {
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtNewsTitle, txtDaysAgo;
        private ImageView newsImage;
    }

    public boolean ismSearching() {
        return mSearching;
    }

    public int getActualEventPos(int selectedSearchpos) {
        if (selectedSearchpos < mValidSearchIndices.size()) {
            return mValidSearchIndices.get(selectedSearchpos);
        } else {
            return 0;
        }
    }

    private void getStat(String stat) {

    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate) {
        try {
            Date newDateFormat = new Date();
            String newDate = format.format(newDateFormat);
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}