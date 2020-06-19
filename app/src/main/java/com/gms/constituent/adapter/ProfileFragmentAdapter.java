package com.gms.constituent.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gms.constituent.fragment.ConstituencyInfoFragment;
import com.gms.constituent.fragment.EnquiryFragment;
import com.gms.constituent.fragment.PetitionFragment;
import com.gms.constituent.fragment.ProfileInfoFragment;

public class ProfileFragmentAdapter extends FragmentStatePagerAdapter {

    public ProfileFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileInfoFragment();
            case 1:
                return new ConstituencyInfoFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}