package com.android.theupdates.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.theupdates.entites.SideBarItem;
import com.android.theupdates.fragments.WeatherAdminFragment;
import com.android.theupdates.fragments.WeatherUserFragment;

/**
 * Created by osamarahat on 10/12/2016.
 */

public class WeatherUpdateAdapter extends FragmentStatePagerAdapter {

    //TODO ADD FIELD FOR ADMIN & USER FOR POST CREATION..
    private SideBarItem sideBarItem;

    public WeatherUpdateAdapter(FragmentManager fm, SideBarItem sideBarItem) {
        super(fm);
        this.sideBarItem = sideBarItem;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return WeatherAdminFragment.getInstance(sideBarItem);
        else
            return WeatherUserFragment.getInstance(sideBarItem);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position==0?"Admin Posts":"User Posts";
    }
}
