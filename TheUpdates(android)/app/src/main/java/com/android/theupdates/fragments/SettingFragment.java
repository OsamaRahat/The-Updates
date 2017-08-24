package com.android.theupdates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.viewbinder.SettingItemBinder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class SettingFragment extends BaseFragment {


    @BindView(R.id.lstViwSettings)
    ListView lstViwSettings;

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSettingList();
    }

    private void getSettingList() {
        ArrayList<String> lstSetting = new ArrayList<String>();

        String[] arrSetting = getMainActivityContext().getResources().getStringArray(R.array.arrSetting);

        for (int i = 0; i < arrSetting.length; i++) {
            lstSetting.add(arrSetting[i]);
        }

        ArrayListAdapter<String> listAdapter = new ArrayListAdapter<String>(getMainActivityContext(), lstSetting, new SettingItemBinder());
        lstViwSettings.setAdapter(listAdapter);
        lstViwSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        getMainActivityContext().addFragment(PrivacyPolicyFragment.getInstance().getClass().getName(),PrivacyPolicyFragment.getInstance());
                        break;
                    case 1:
                        getMainActivityContext().addFragment(GroupRulesFragment.getInstance().getClass().getName(),GroupRulesFragment.getInstance());
                        break;
                    case 2:
                        UIHelper.showLongToastInCenter(getMainActivityContext(),"In Progress");
                        //getMainActivityContext().addFragment(GroupRulesFragment.getInstance().getClass().getName(),GroupRulesFragment.getInstance());
                        break;
                    case 3:
                        getMainActivityContext().addFragment(FeedBackFragment.getInstance().getClass().getName(),FeedBackFragment.getInstance());
                        break;

                    case 4:
                        UIHelper.showLongToastInCenter(getMainActivityContext(),"In Progress");
                        break;

                    case 5:
                        getMainActivityContext().addFragment(AboutFragment.getInstance().getClass().getName(),AboutFragment.getInstance());
                        break;
                }
            }
        });


    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Settings");
        toolbar.setBackgroundResource(R.color.green_app);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().drawer.openDrawer(GravityCompat.START);
            }
        });

        setStatusBarColor(R.color.green_app);
    }


}
