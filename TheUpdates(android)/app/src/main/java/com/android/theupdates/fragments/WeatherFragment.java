package com.android.theupdates.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.theupdates.R;
import com.android.theupdates.adapter.WeatherUpdateAdapter;
import com.android.theupdates.entites.SideBarItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class WeatherFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.pagerSlider)
    ViewPager pagerSlider;

    SideBarItem sideBarItem;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;

    public static WeatherFragment getInstance(SideBarItem sideBarItem) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(UPDATE_OBJ, sideBarItem);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sideBarItem = (SideBarItem) getArguments().get(UPDATE_OBJ);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weatherupdates, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pagerSlider.setAdapter(new WeatherUpdateAdapter(getChildFragmentManager(), sideBarItem));
        pagerSlider.setCurrentItem(0, true);

        slidingTabs.setupWithViewPager(pagerSlider);

    }

    Toolbar toolbar = null;

    @Override
    public void setTitleBar(Toolbar toolbar) {
        this.toolbar = toolbar;
        getMainActivityContext().mainToolbarTitle.setText(sideBarItem.getStrCaption());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setBackgroundColor(getMainActivityContext().getResources().getColor(sideBarItem.getColorBackground(), null));
        } else {
            toolbar.setBackgroundColor(getMainActivityContext().getResources().getColor(sideBarItem.getColorBackground()));
        }
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().drawer.openDrawer(GravityCompat.START);
            }
        });
        setStatusBarColor(sideBarItem.getColorBackground());


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}
