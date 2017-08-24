package com.android.theupdates.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.ContentText;
import com.android.theupdates.entites.UpdatesGroup;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponseList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class UpdateItemRulesFragment extends BaseFragment {


    @BindView(R.id.txtContent)
    TextView txtContent;

    private UpdatesGroup updatesGroup;
    WebResponseList<ContentText> webResponse;

    public static UpdateItemRulesFragment getInstance(UpdatesGroup updatesGroup) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UPDATE_OBJ,updatesGroup);
        UpdateItemRulesFragment group = new UpdateItemRulesFragment();
        group.setArguments(bundle);
        return group;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updatesGroup = (UpdatesGroup) getArguments().get(UPDATE_OBJ);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacypolicy, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(webResponse != null)
        {
            txtContent.setText(webResponse.getData().get(0).getContentText());
            return;
        }

        getMainActivityContext().onLoadingStarted();

        Call<WebResponseList<ContentText>> response = getWebServiceInstance().getUpdatesContent(getUserId(),updatesGroup.getGroupSlug(),1);
        response.enqueue(new CustomWebResponse<WebResponseList<ContentText>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<ContentText>> call, Response<WebResponseList<ContentText>> response) {

                getMainActivityContext().onLoadingFinished();

                webResponse = response.body();
                if(webResponse != null && webResponse.isSucceed(response,getMainActivityContext())) {
                    txtContent.setText(webResponse.getData().get(0).getContentText());
                }
            }
        });

    }


    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText(updatesGroup.getGroupName());
        toolbar.setBackgroundColor(Color.parseColor(updatesGroup.getGroupHexColor()));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });

        setStatusBarColor(updatesGroup.getGroupHexColor());
    }


}
