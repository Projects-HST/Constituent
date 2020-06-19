package com.gms.constituent.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gms.constituent.fragment.EnquiryFragment;
import com.gms.constituent.fragment.PetitionFragment;

public class GrievanceFragmentAdapter extends FragmentStatePagerAdapter {

    public GrievanceFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PetitionFragment();
            case 1:
                return new EnquiryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}