package com.android.theupdates.helper;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.theupdates.entites.UserInfo;
import com.android.theupdates.webapi.GsonFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class TheUpdatesPreferenceHelper extends PreferenceHelper {

    private static final String FILENAME = "prefrences";

    protected static final String KEY_USER = "user";
    protected static final String KEY_LANGUAGE = "language";
    protected static final String KEY_IS_FIRST_TIME = "isFirstTime";
    protected static final String KEY_ADZ = "adz";
    protected static final String KEY_ANNOUCEMENT_LST = "announcement";
    protected static final String KEY_ACCOUNT_USER = "account_user";
    protected static final String KEY_DEVICE_TOKEN = "token";

    Context context;


    public TheUpdatesPreferenceHelper(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public UserInfo getUser() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_USER), UserInfo.class);
    }

    public void putUser(UserInfo user) {
        putStringPreference(context, FILENAME, KEY_USER, GsonFactory
                .getConfiguredGson().toJson(user));
    }

    public String getDeviceToken() {
        return getStringPreference(context, FILENAME, KEY_DEVICE_TOKEN);
    }

    public void putDeviceToken(String token) {
        putStringPreference(context, FILENAME, KEY_DEVICE_TOKEN, token);
    }

    public String getIsFirstTime() {
        return getStringPreference(context, FILENAME, KEY_IS_FIRST_TIME);
    }

    public void putIsFirstTime(Boolean isCheck) {
        putBooleanPreference(context, FILENAME, KEY_IS_FIRST_TIME, isCheck);

    }

    public boolean getAdzAvailability() {
        return getBooleanPreference(context, FILENAME, KEY_ADZ);
    }

    public void putAdzClose(Boolean isCheck) {
        putBooleanPreference(context, FILENAME, KEY_ADZ, isCheck);

    }

    public void putAnnouncementLst(ArrayList<String> lstDismissAnnouncement) {

        putStringPreference(context, FILENAME, KEY_ANNOUCEMENT_LST,
                getAnnouncemenJson(lstDismissAnnouncement));
    }

    public String getAnnouncemenJson(ArrayList<String> lstDismissAnnouncement) {
        return GsonFactory.getConfiguredGson().toJson(lstDismissAnnouncement);
    }

    public ArrayList<String> getAnnouncementLst() {
        String json = getStringPreference(context, FILENAME, KEY_ANNOUCEMENT_LST);
        if (TextUtils.isEmpty(json))
            return new ArrayList<String>();
        return getAnnouncemenLstFromJson(json);
    }

    private ArrayList<String> getAnnouncemenLstFromJson(String json) {
        Type collectionType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return GsonFactory.getConfiguredGson().fromJson(json, collectionType);

    }

    public String getAccountUser() {
        return getStringPreference(context, FILENAME, KEY_ACCOUNT_USER);

    }

    public void putAccountUser(String user) {
        putStringPreference(context, FILENAME, KEY_ACCOUNT_USER, user);
    }


}
