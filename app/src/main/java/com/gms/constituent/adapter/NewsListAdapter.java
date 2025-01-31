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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

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
//            holder.newsFeedLayout = (RelativeLayout) convertView.findViewById(R.id.news_feed_layout);
            holder.txtNewsTitle = (TextView) convertView.findViewById(R.id.txt_news_name);
            holder.txtNewsDetails = (TextView) convertView.findViewById(R.id.txt_news);
            holder.txtDaysAgo = (TextView) convertView.findViewById(R.id.txt_news_date);
            holder.newsImage = (ImageView) convertView.findViewById(R.id.img_logo);
            convertView.setTag(holder);
        } else {
            holder = (NewsListAdapter.ViewHolder) convertView.getTag();
//            holder.newsFeedLayout = (RelativeLayout) convertView.findViewById(R.id.news_feed_layout);
            holder.txtNewsTitle = (TextView) convertView.findViewById(R.id.txt_news_name);
            holder.txtNewsDetails = (TextView) convertView.findViewById(R.id.txt_news);
            holder.txtDaysAgo = (TextView) convertView.findViewById(R.id.txt_news_date);
            holder.newsImage = (ImageView) convertView.findViewById(R.id.img_logo);
        }
        holder.txtNewsTitle.setText(capitalizeString(news.get(position).gettitle()));
        holder.txtNewsTitle.setEllipsize(TextUtils.TruncateAt.END);

        holder.txtNewsDetails.setText(HtmlCompat.fromHtml(news.get(position).getdetails(),
                HtmlCompat.FROM_HTML_MODE_LEGACY));
//        holder.txtNewsDetails.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtDaysAgo.setText(getserverdateformat(news.get(position).getnews_date()));
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
        public TextView txtNewsTitle, txtNewsDetails, txtDaysAgo;
        private RelativeLayout newsFeedLayout;
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

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String getDateDiff(SimpleDateFormat format, String oldDate) {
        try {
            Date newDateFormat = new Date();
            String newDate = format.format(newDateFormat);
            String dateshow = "";
            long finaldays;
            long days = TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            if (days >= 0 ) {
                if (days >= 365) {
                    finaldays = (days / 365);
                    dateshow = finaldays + " Years Ago";
                } else {
                    if (days >= 30) {
                        finaldays = (days / 30);
                        dateshow = finaldays + " Months Ago";
                    } else {
                        if (days >= 7) {
                            finaldays = (days / 7);
                            dateshow = finaldays + " Weeks Ago";
                        } else if (days == 0) {
                            dateshow = "Today";
                        } else {
                            dateshow = days + " Days Ago";
                        }
                    }
                }
            } else {
                if (days <= -365) {
                    finaldays = (days / -365);
                    dateshow = "In "+ finaldays + " Years";
                } else {
                    if (days <= -30) {
                        finaldays = (days / -30);
                        dateshow = "In "+ finaldays + " Months";
                    } else {
                        if (days <= -7) {
                            finaldays = (days / -7);
                            dateshow = "In "+ finaldays + " Weeks";
                        } else {
                            finaldays = (days / -1);
                            dateshow = "In "+ finaldays + " Days";
                        }
                    }
                }
            }
            return dateshow;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
    }

}