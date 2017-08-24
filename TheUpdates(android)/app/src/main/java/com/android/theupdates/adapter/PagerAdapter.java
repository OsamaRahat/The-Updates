package com.android.theupdates.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.theupdates.entites.PostItem;
import com.android.theupdates.fragments.ScreenSlidePageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osamarahat on 06/11/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> imagesColloection;
    PostItem postItem;

    public PagerAdapter(FragmentManager fm, List<String> imagesColloection, PostItem postItem) {
        super(fm);
        this.imagesColloection = new ArrayList<String>();
        this.imagesColloection.addAll(imagesColloection);
        this.postItem = postItem;
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.getInstance(imagesColloection.get(position),postItem);
    }

    @Override
    public int getCount() {
        return imagesColloection.size();
    }
}