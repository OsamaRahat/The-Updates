package com.android.theupdates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.entites.UpdatesGroup;
import com.android.theupdates.viewbinder.UpdateItemRulesBinder;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponseList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class GroupRulesFragment extends BaseFragment {


    @BindView(R.id.lstViwSettings)
    ListView lstViwSettings;

    ArrayList<UpdatesGroup> arrGroup = null;
    ArrayListAdapter<UpdatesGroup> listAdapter = null;

    public static GroupRulesFragment getInstance() {
        return new GroupRulesFragment();
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

        if (arrGroup != null && arrGroup.size() > 0) {
            listAdapter.notifyDataSetChanged();
            lstViwSettings.setAdapter(listAdapter);

        } else {
            getSettingList();
        }

        lstViwSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getMainActivityContext().addFragment(UpdateItemRulesFragment.getInstance(arrGroup.get(i)).getClass().getName(), UpdateItemRulesFragment.getInstance(arrGroup.get(i)));
            }
        });
    }

    private void getSettingList() {
        arrGroup = new ArrayList<UpdatesGroup>();
        getMainActivityContext().onLoadingStarted();
        Call<WebResponseList<UpdatesGroup>> response = getWebServiceInstance().getUpdatesGroup(getUserId());
        response.enqueue(new CustomWebResponse<WebResponseList<UpdatesGroup>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<UpdatesGroup>> call, Response<WebResponseList<UpdatesGroup>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<UpdatesGroup> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    arrGroup.addAll(webResponse.getData());

                    listAdapter = new ArrayListAdapter<UpdatesGroup>(getMainActivityContext(), arrGroup, new UpdateItemRulesBinder());
                    lstViwSettings.setAdapter(listAdapter);

                }
            }

        });


    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Group Rules");
        toolbar.setBackgroundResource(R.color.green_app);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });

        setStatusBarColor(R.color.green_app);
    }
}
