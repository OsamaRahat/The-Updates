package com.android.theupdates.webapi;

import android.content.Context;

import com.android.theupdates.helper.UIHelper;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by osamarahat on 22/10/2016.
 */

public class WebResponse<T> {
    private String status;
    private T data;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public boolean isSucceed() {
//        return getStatus().equals("1") ? true : false;
//    }

    public boolean isSucceed(Response response, Context context) {

        if(context == null)
            return false;

        try {
            if (response.errorBody() == null) {
                if (response.code() != 200) {
                    UIHelper.showLongToastInCenter(context, response.errorBody().string());
                    return false;
                }
                else
                    return getStatus().equals("1") ? true : false;
            }
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
