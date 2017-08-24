package com.android.theupdates;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.theupdates.helper.TheUpdatesPreferenceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class AppLoader extends MultiDexApplication {

    static TheUpdatesPreferenceHelper preferenceHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        initImageConf();
        ImageLoader.getInstance().init(initImageConf().build());
        preferenceHelper = new TheUpdatesPreferenceHelper(getApplicationContext());
    }

    private ImageLoaderConfiguration.Builder initImageConf() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        return config;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    private static boolean activityVisible;
    private static boolean isAdzAvailable=true;

    public static boolean isAdzAvailable() {
        return preferenceHelper.getAdzAvailability();
    }

    public static void setIsAdzAvailable(boolean isAdzAvailable) {
        AppLoader.isAdzAvailable = isAdzAvailable;
        preferenceHelper.putAdzClose(isAdzAvailable);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(AppLoader.this);
    }
}
