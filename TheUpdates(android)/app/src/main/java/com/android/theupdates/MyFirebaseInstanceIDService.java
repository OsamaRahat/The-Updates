package com.android.theupdates;

import android.util.Log;

import com.android.theupdates.callbacks.RegistrationPushNotification;
import com.android.theupdates.constants.CommonUtility;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by osamarahat on 07/12/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements RegistrationPushNotification{

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        registerPushNotification();
    }



    @Override
    public void registerPushNotification() {
        CommonUtility.sendDeviceToken(getApplicationContext());
    }


}
