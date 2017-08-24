package com.android.theupdates.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.theupdates.AppLoader;

import java.util.Calendar;

/**
 * Created by osamarahat on 24/10/2016.
 */

public class AdzDisplayReceiver extends BroadcastReceiver {

    private static final long PERIOD=86400000L;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(AppLoader.isActivityVisible() && context != null)
        {
            MainActivity.instance().setEnableAdz();
            AppLoader.setIsAdzAvailable(true);
        }
        else
            AppLoader.setIsAdzAvailable(true);
    }

    public static void scheduleAlarms(MainActivity ctxt) {
        AlarmManager mgr=
                (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(ctxt, AdzDisplayReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);

//        mgr.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +
//                PERIOD, 86400000L, pi);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+1);


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        mgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                 pi);
    }
}
