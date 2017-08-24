package com.android.theupdates.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by osamarahat on 24/10/2016.
 */

public class ScheduleAdz extends IntentService {

    public ScheduleAdz(String name) {
        super(name);
    }

    public ScheduleAdz() {
        super("SCHEDULE_ADZ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        if(AppLoader.isActivityVisible() && getApplicationContext() != null)
//        {
//            ((MainActivity)getApplicationContext()).adzLine.setVisibility(View.VISIBLE);
//            AppLoader.setIsAdzAvailable(true);
//        }
//        else
//            AppLoader.setIsAdzAvailable(true);
    }
}
