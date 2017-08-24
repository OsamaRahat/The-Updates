package com.android.theupdates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.ContentText;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponseList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.android.theupdates.constants.Constants.SLUG_TERMS_COND;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class TermsConditionFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.imgBtnAccept)
    TextView imgBtnAccept;
    @BindView(R.id.txtContent)
    TextView txtContent;

    WebResponseList<ContentText> webResponse;

    public static TermsConditionFragment getInstance() {
        return new TermsConditionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_n_condition, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivityContext().setDisableAdz();

        getMainActivityContext().setSideMenu(false);
        imgBtnAccept.setOnClickListener(this);

        if(webResponse != null)
        {
            txtContent.setText(webResponse.getData().get(0).getContentText());
            return;
        }
        getMainActivityContext().onLoadingStarted();

        Call<WebResponseList<ContentText>> response = getWebServiceInstance().getUpdatesContent(getUserId(),SLUG_TERMS_COND, 0);
        response.enqueue(new CustomWebResponse<WebResponseList<ContentText>>(getMainActivityContext()) {

            @Override
            public void onResponse(Call<WebResponseList<ContentText>> call, Response<WebResponseList<ContentText>> response) {

                getMainActivityContext().onLoadingFinished();

                webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response,getMainActivityContext())) {
                    txtContent.setText(webResponse.getData().get(0).getContentText());
                }
            }


        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnAccept:
                getMainActivityContext().preferenceHelper.putIsFirstTime(true);
                getMainActivityContext().emptyBackStack();
                getMainActivityContext().setEnableAdz();
                getMainActivityContext().addFragmentOnce(HomeFragment.getInstance().getClass().getName(), HomeFragment.getInstance());
                break;
        }
    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Terms & Conditions");
        toolbar.setBackgroundResource(R.color.green_app);
        setStatusBarColor(R.color.green_app);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });
    }
}
