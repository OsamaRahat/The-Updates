package com.android.theupdates.fragments;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.theupdates.R;
import com.android.theupdates.activities.MainActivity;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.android.theupdates.webapi.WebService;
import com.android.theupdates.webapi.WebServiceFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import retrofit2.Call;
import retrofit2.Response;


public abstract class BaseFragment extends Fragment {

    private MainActivity mainActivityContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityContext = (MainActivity) context;
    }

    public MainActivity getMainActivityContext() {
        return mainActivityContext;
    }

    public WebService getWebServiceInstance() {
        return WebServiceFactory.getInstance();
    }


    public boolean isSucceed(WebResponse webResponse) {
        return webResponse.getStatus().equals("1") ? true : false;

    }

    public abstract void setTitleBar(Toolbar toolbar);

    @Override
    public void onResume() {
        super.onResume();
        setTitleBar(getMainActivityContext().toolbar);
    }


    public String getEditTxt(EditText editText) {
        return editText.getText().toString();
    }

    public String getUserId() {
        return getMainActivityContext().preferenceHelper.getUser().getUserId();

    }

    public void setStatusBarColor(int color) {
        Window window = getMainActivityContext().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getMainActivityContext(), color));
        }
    }

    public void setStatusBarColor(String color) {
        Window window = getMainActivityContext().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public DisplayImageOptions getImageOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.splash)
                .showImageOnFail(R.drawable.splash)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        return defaultOptions;
    }

    public void reportUserPost(String postId, String toUserId) {

        getMainActivityContext().onLoadingStarted();
        Call<WebResponse<String>> response = getWebServiceInstance().reportPost(getUserId(),postId,toUserId);
        response.enqueue(new CustomWebResponse<WebResponse<String>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<String>> call, Response<WebResponse<String>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<String> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    UIHelper.showLongToastInCenter(getMainActivityContext(),webResponse.getMessage());
                }

            }


        });

    }

    public void reportUserPost(String postId, String toUserId,String reason) {

        getMainActivityContext().onLoadingStarted();
        Call<WebResponse<String>> response = getWebServiceInstance().reportUser(getUserId(),postId,toUserId,reason);
        response.enqueue(new CustomWebResponse<WebResponse<String>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<String>> call, Response<WebResponse<String>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<String> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    UIHelper.showLongToastInCenter(getMainActivityContext(),webResponse.getMessage());
                }

            }


        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //getMainActivityContext().toolbar.setBackgroundResource(R.color.green_app);
        //getMainActivityContext().mainToolbarTitle.setText("The Updates");
        //setStatusBarColor(R.color.green_app);
    }


}
