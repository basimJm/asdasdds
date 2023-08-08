package com.schoofi.activitiess;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digits.sdk.android.Digits;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.schoofi.activities.LoginScreen;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.activities.TeacherHomeScreen;
import com.schoofi.adapters.TeacherCircularViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;

import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;


import com.schoofi.utils.VolleySingleton;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.schoofi.activitiess.SplashScreen;
import android.app.PendingIntent;
//import io.fabric.sdk.android.Fabric;

@SuppressWarnings("deprecation")
public class SplashScreen extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

	GoogleCloudMessaging gcm;
	Context context;
	String regId;
	String msg = "";

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";
	private JSONArray versionArray;
	String version;
	int count =0;

	//private InterstitialAd mInterstitialAd;

	private String TAG1 = SplashScreen.class.getSimpleName();
	InterstitialAd mInterstitialAd;
	String deviceId,androidId,address;
	TextToSpeech t1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		// Set layout
		setContentView(R.layout.activity_splash_screen);

		// Load user preferences
		Preferences.getInstance().loadPreference(SplashScreen.this);

		// Get Google Analytics Tracker
		Tracker t = ((SchoofiApplication) this.getApplication()).getTracker(
				TrackerName.APP_TRACKER);

		// Set screen name for Google Analytics
		t.setScreenName("Splash Screen");

		// Send a screen view event to Google Analytics
		t.send(new HitBuilders.ScreenViewBuilder().build());

		// Initialize context
		context = getApplicationContext();

		Thread splash = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// Load user preferences again (duplicated)
					Preferences.getInstance().loadPreference(SplashScreen.this);

					// Delay based on user preferences
					int delayMillis = (Preferences.getInstance().deviceId == null) ? 20000 : 5000;
					Thread.sleep(delayMillis);

					// Get app version
					version = BuildConfig.VERSION_NAME;

					// Get student feed list based on user role
					getStudentFeedList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		splash.start();

		Intent intent = new Intent(this, SplashScreen.class); // Replace with your target activity
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	private void showInterstitial() {
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	protected void getStudentFeedList()
	{
								Preferences.getInstance().loadPreference(getApplicationContext());
								if (!Preferences.getInstance().isLoggedIn) {
									Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
									Log.d("po","lk");
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("6") && Preferences.getInstance().fullRoleId.matches("6") ) {
									Intent intent = new Intent(SplashScreen.this, ParentHomeScreen.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("5") && Preferences.getInstance().fullRoleId.matches("5")) {
									Intent intent = new Intent(SplashScreen.this, StudentHomeScreen.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("4") && Preferences.getInstance().fullRoleId.matches("4")) {
									Intent intent = new Intent(SplashScreen.this, TeacherHomeScreen.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("7") && Preferences.getInstance().fullRoleId.matches("7")) {
									Intent intent = new Intent(SplashScreen.this, ChairmanHomeScreen.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("8") && Preferences.getInstance().fullRoleId.matches("8")) {
									Intent intent = new Intent(SplashScreen.this, ChairmanDashboard.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("0") && Preferences.getInstance().fullRoleId.matches("0")) {
									Intent intent = new Intent(SplashScreen.this, AuditUserHomeScreen.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("3") && Preferences.getInstance().fullRoleId.matches("3")) {
									Intent intent = new Intent(SplashScreen.this, AdminHomeScreen.class);
									startActivity(intent);
								} else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("9") && Preferences.getInstance().fullRoleId.matches("9")) {
									Intent intent = new Intent(SplashScreen.this, AdmissionEnquiry.class);
									startActivity(intent);
								}

								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("10") && Preferences.getInstance().fullRoleId.matches("10")) {
									Intent intent = new Intent(SplashScreen.this, BusAdminHomeScreen.class);
									startActivity(intent);
								}

								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("11") && Preferences.getInstance().fullRoleId.matches("11")) {
									Intent intent = new Intent(SplashScreen.this, FeeAdminScreen.class);
									startActivity(intent);
								}
								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("15") && Preferences.getInstance().fullRoleId.matches("15")) {
									Intent intent = new Intent(SplashScreen.this, MedicalCoordinatorHomeScreen.class);
									startActivity(intent);
								}

								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("16") && Preferences.getInstance().fullRoleId.matches("16")) {
									Intent intent = new Intent(SplashScreen.this, BusCoordinatorAdminScreen.class);
									startActivity(intent);
								}

								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("17") && Preferences.getInstance().fullRoleId.matches("17")) {
									Intent intent = new Intent(SplashScreen.this, SecurityAdminScreen.class);
									startActivity(intent);
								}

								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("26") && Preferences.getInstance().fullRoleId.matches("26")) {
									Intent intent = new Intent(SplashScreen.this, NonTeachingHomeScreen.class);
									startActivity(intent);
								}

								else if (Preferences.getInstance().isLoggedIn && Preferences.getInstance().userRoleId.matches("27") && Preferences.getInstance().fullRoleId.matches("27")) {
									Intent intent = new Intent(SplashScreen.this, HRHomeScreen.class);
									startActivity(intent);
								}
								else
								{
									Intent intent = new Intent(SplashScreen.this, RoleLoginScreen.class);
									startActivity(intent);
								}
	}

	private void toa()
	{
		System.out.println("aaa");
	}


	}

