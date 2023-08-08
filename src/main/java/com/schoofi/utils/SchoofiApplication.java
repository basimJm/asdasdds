package com.schoofi.utils;

import java.util.HashMap;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.schoofi.activitiess.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.utils.TrackerName;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import android.util.DisplayMetrics;



public class SchoofiApplication  extends MultiDexApplication{
	
	public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }





    private static final String PROPERTY_ID = "UA-71070636-1";
	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!this.mTrackers.containsKey(trackerId)) {
            Tracker t;
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if (trackerId == TrackerName.APP_TRACKER) {
                t = analytics.newTracker(PROPERTY_ID);
            } else if (trackerId == TrackerName.GLOBAL_TRACKER) {
                t = analytics.newTracker((int) R.xml.global_trackers);
            } else {
                t = analytics.newTracker((int) R.xml.ecommerce_tracker);
            }
            this.mTrackers.put(trackerId, t);
        }
        return (Tracker) this.mTrackers.get(trackerId);
    }
	
	 public static float convertDpToPixel(float dp) {
	        return dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
	    }


}



