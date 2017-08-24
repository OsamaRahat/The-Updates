package com.android.theupdates.webapi;

import android.content.Context;

import com.android.theupdates.entites.Announcement;
import com.android.theupdates.entites.UpdatesGroup;
import com.android.theupdates.helper.UIHelper;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by osamarahat on 22/10/2016.
 */

public class WebResponseList<T> {
    private String status;
    private ArrayList<T> data;
    private String message;
    private ArrayList<Announcement> announcement;
    private ArrayList<UpdatesGroup> sidebar;
    private ArrayList<T> pinned;

    public ArrayList<UpdatesGroup> getSidebar() {
        return sidebar==null?new ArrayList<UpdatesGroup>():sidebar;
    }

    public void setSidebar(ArrayList<UpdatesGroup> sidebar) {
        this.sidebar = sidebar;
    }

    public ArrayList<T> getPinned() {
        return pinned==null?new ArrayList<T>():pinned;
    }

    public void setPinned(ArrayList<T> pinned) {
        this.pinned = pinned;
    }

    public ArrayList<Announcement> getAnnouncement() {
        return announcement==null?new ArrayList<Announcement>():announcement;
    }

    public void setAnnouncement(ArrayList<Announcement> announcement) {
        this.announcement = announcement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public boolean isSucceed()
//    {
//        return getStatus().equals("1")?true:false;
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
