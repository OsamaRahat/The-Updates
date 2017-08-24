package com.android.theupdates.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.theupdates.AppLoader;
import com.android.theupdates.R;
import com.android.theupdates.activities.AdzDisplayReceiver;
import com.android.theupdates.entites.ADzData;
import com.android.theupdates.ui.views.CustomButton;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class AdvertisementFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.imgAd)
    ImageView imgAd;
    @BindView(R.id.imgClose)
    CustomButton imgClose;

    WebResponse<ADzData> webResponse;

    public static AdvertisementFragment getInstance() {
        return new AdvertisementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adz, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivityContext().adzBgLine.setVisibility(View.GONE);

        imgAd.setOnClickListener(this);
        imgClose.setOnClickListener(this);

        getMainActivityContext().setDisableAdz();

        getMainActivityContext().onLoadingStarted();

        Call<WebResponse<ADzData>> response = getWebServiceInstance().getAdz(getUserId());
        response.enqueue(new CustomWebResponse<WebResponse<ADzData>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<ADzData>> call, Response<WebResponse<ADzData>> response) {

                getMainActivityContext().onLoadingFinished();

                webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    ImageLoader.getInstance().displayImage(webResponse.getData().getAddImage(),imgAd);
                }
            }


        });


    }


    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("The Updates");
        toolbar.setNavigationIcon(null);
        toolbar.setBackgroundResource(R.color.green_app);
        setStatusBarColor(R.color.green_app);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgClose:
                getMainActivityContext().onBackPressed();
                break;

            case R.id.imgAd:
                try {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(webResponse.getData().getAddURL()));
                    startActivity(webIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLoader.setIsAdzAvailable(false);
        getMainActivityContext().adzBgLine.setVisibility(View.GONE);
        AdzDisplayReceiver.scheduleAlarms(getMainActivityContext());
    }
}
