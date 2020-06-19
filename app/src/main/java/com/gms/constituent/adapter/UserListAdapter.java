package com.gms.constituent.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.gms.constituent.R;
import com.gms.constituent.bean.support.User;
import com.gms.constituent.utils.GMSValidator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<User> users;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();


    public UserListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
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
            return users.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return users.get(mValidSearchIndices.get(position));
        } else {
            return users.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_user, parent, false);

            holder = new UserListAdapter.ViewHolder();
            holder.txtUserName = (TextView) convertView.findViewById(R.id.user_name);
            holder.txtUserSerial = (TextView) convertView.findViewById(R.id.user_serial);
            holder.txtUserDOB = (TextView) convertView.findViewById(R.id.user_dob);
            holder.userImage = (ImageView) convertView.findViewById(R.id.user_image);
            convertView.setTag(holder);
        } else {
            holder = (UserListAdapter.ViewHolder) convertView.getTag();
            holder.txtUserName = (TextView) convertView.findViewById(R.id.user_name);
            holder.txtUserSerial = (TextView) convertView.findViewById(R.id.user_serial);
            holder.txtUserDOB = (TextView) convertView.findViewById(R.id.user_dob);
            holder.userImage = (ImageView) convertView.findViewById(R.id.user_image);

        }

        holder.txtUserName.setText(users.get(position).getfull_name());
        holder.txtUserSerial.setText("Serial number - " +users.get(position).getSerial_no());
        holder.txtUserDOB.setText("Date of birth - " +users.get(position).getdob());
        if (GMSValidator.checkNullString(users.get(position).getprofile_picture())) {
            Picasso.get().load(users.get(position).getprofile_picture()).into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_profile);
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
        for (int i = 0; i < users.size(); i++) {
            String homeWorkTitle = users.get(i).getid();
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
        public TextView txtUserName, txtUserSerial, txtUserDOB;
        private ImageView userImage;
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

}