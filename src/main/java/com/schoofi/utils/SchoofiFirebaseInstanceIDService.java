package com.schoofi.utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Schoofi on 10-10-2017.
 */

public class SchoofiFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        Preferences.getInstance().loadPreference(getApplicationContext());
        Preferences.getInstance().deviceId = refreshedToken;
        Preferences.getInstance().savePreference(getApplicationContext());

        Log.d("po",Preferences.getInstance().deviceId);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}

