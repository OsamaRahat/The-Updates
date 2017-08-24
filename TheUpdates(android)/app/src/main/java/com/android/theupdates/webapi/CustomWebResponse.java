package com.android.theupdates.webapi;

import android.content.Context;

import com.android.theupdates.activities.MainActivity;
import com.android.theupdates.helper.UIHelper;

import java.net.HttpRetryException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class CustomWebResponse<T> implements Callback<T> {

    private Context context;
    private Response<T> response;

    public CustomWebResponse(Context context) {
        this.context = context;

    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {

        ((MainActivity) context).onLoadingFinished();

        if (t instanceof OutOfMemoryError) {
            UIHelper.showLongToastInCenter(context, "Please try again later.");
        }
        else if (t instanceof SocketTimeoutException) {
            UIHelper.showLongToastInCenter(context, "Connection timeout");
        } else if (t instanceof HttpRetryException) {
            HttpRetryException response = (HttpRetryException) t;
            int code = response.responseCode();
            if (code != 200) {
                UIHelper.showLongToastInCenter(context, t.getMessage());
            }
        }

        else if(t instanceof UnknownHostException)
        {
            UIHelper.showLongToastInCenter(context, "Connectivity Error Occur..");
        }
    }


    public void setResponse(Response<T> response) {
        this.response = response;
    }
}
