package com.example.diamkpo.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diamkpo.Fragments.StaffSignInFragment;
import com.example.diamkpo.Fragments.UserSignInFragment;

import org.jetbrains.annotations.NotNull;

public class TabAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserSignInFragment userSignInFragment = new UserSignInFragment();
                return userSignInFragment;
            case 1:
                StaffSignInFragment staffSignInFragment = new StaffSignInFragment();
                return staffSignInFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
