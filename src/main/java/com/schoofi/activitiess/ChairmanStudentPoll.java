package com.schoofi.activitiess;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.HomeUserAdapter;
import com.schoofi.adapters.StudentPolllListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;

public class ChairmanStudentPoll extends AppCompatActivity implements OnItemClickListener {

	private JSONArray studentPollArray;
	private ListView studentPollListView;
	StudentPolllListAdapter studentPolllListAdapter;
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userType = Preferences.getInstance().userType;
	private DrawerLayout drawerlayout;
	private ActionBarDrawerToggle drawerListener;
	ActionBar actionBar;
	private ListView listView;
	private HomeUserAdapter homeUserAdapter;
	private SwipyRefreshLayout swipyRefreshLayout;
	ArrayList<String> permissionArray1 = new ArrayList<String>();
	private JSONArray schoolNotificationListArray;
	String url1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
		getSupportActionBar().setTitle("Polls");
		setContentView(R.layout.activity_chairman_student_poll);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentPoll");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		studentPollListView = (ListView) findViewById(R.id.listView_students_polls);
		drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		listView = (ListView) findViewById(R.id.drawerList);
		homeUserAdapter = new HomeUserAdapter(this,permissionArray1);
		listView.setAdapter(homeUserAdapter);
		listView.setOnItemClickListener(this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		drawerListener = new ActionBarDrawerToggle(ChairmanStudentPoll.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close)
		{
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub

			}
		};

		drawerlayout.setDrawerListener(drawerListener);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				swipyRefreshLayout.setRefreshing(true);
				initData();
				getStudentPollList();
			}
		});
		//drawerlayout.openDrawer(listView);
		studentPollListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub


				try {
					Intent intent = new Intent(ChairmanStudentPoll.this,NewSubmittedPollQuestionList.class);
					intent.putExtra("position", position);
					intent.putExtra("poll_id",studentPollArray.getJSONObject(position).getString("poll_id"));
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}



			}

		});

		initData();
		getStudentPollList();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this,ChairmanDashboard.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(ChairmanStudentPoll.this,ChairmanDashboard.class);
		startActivity(intent);
		this.finish();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chairman_school_attendance, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if(drawerListener.onOptionsItemSelected(item))
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub


		if(Preferences.getInstance().userRoleId.matches("7"))
		{


		switch(position)
		{
		case 0 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 1:
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
			
			

		case 2:
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, AboutUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, AboutSchool.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 3 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 4 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 5 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

			case 6 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;


		case 7 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
			startActivity(intent);
			finish();

			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
		{

			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
			intent.putExtra("url", "https://www.schoofi.com/");
			intent.putExtra("position",0);
			startActivity(intent);
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
		{
			getStudentFeedList();
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
			Preferences.getInstance().isLoggedIn = false;
			Preferences.getInstance().savePreference(getApplicationContext());
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
		{
			Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

			startActivity(intent);
			finish();
			break;
		}

			break;

		case 8 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 9 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 10 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 11 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
			
		case 12 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

			case 13:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 14:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 15:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 16:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

			case 17:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}
			case 18:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}



				break;


			case 19:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{

					break;
				}




		default:

			break;
		
		}
		}
		
		else
		{
			switch(position)
			{
				case 0 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 1:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;



				case 2:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, AboutUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, AboutSchool.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 3 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 4 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 5 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 6 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;


				case 7 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

					break;

				case 8 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 9 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 10 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 11 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 12 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 13:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 14:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 15:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 16:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

				case 17:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 18:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{

						break;
					}

					break;

			default:

				break;
			}
		}

	}

	protected void getStudentFeedList()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_WEBSITE_URL + "?ins_id=" + Preferences.getInstance().institutionId+"&sch_id=" + Preferences.getInstance().schoolId;
		StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					//toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Intent intent = new Intent(ChairmanStudentPoll.this,AboutSchool.class);
						startActivity(intent);
					}
					//Utils.showToast(A,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("school"))
					{
						schoolNotificationListArray= new JSONObject(response).getJSONArray("school");
						if(null!=schoolNotificationListArray && schoolNotificationListArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = schoolNotificationListArray.toString().getBytes();
							VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
							url1 = responseObject.getJSONArray("school").getJSONObject(0).getString("school_website_url");
							if(url1.matches("") || url1.matches("null"))
							{
								Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
							}

							else {

								Intent intent = new Intent(ChairmanStudentPoll.this, WebViewActivity.class);
								intent.putExtra("url", url1);
								intent.putExtra("position",0);
								startActivity(intent);
							}

						}
					}
					else
						Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(getApplicationContext()))
			queue.add(requestObject);
		else
		{
			Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
		}
	}

	private void initData()
	{


		try
		{
			Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				studentPollArray= null;
			}
			else
			{
				studentPollArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentPollArray!= null)
		{
			studentPollListView.invalidateViews();
			studentPolllListAdapter= new StudentPolllListAdapter(ChairmanStudentPoll.this,studentPollArray);
			studentPollListView.setAdapter(studentPolllListAdapter);
			studentPolllListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentPollList()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentPoll.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(ChairmanStudentPoll.this,"No Records Found");
						studentPollListView.setVisibility(View.GONE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(ChairmanStudentPoll.this, "Session Expired:Please Login Again");
						studentPollListView.setVisibility(View.GONE);
					}
					else
					if(responseObject.has("poll_List"))
					{
						studentPollArray= new JSONObject(response).getJSONArray("poll_List");
						studentPollListView.setVisibility(View.VISIBLE);
						if(null!=studentPollArray && studentPollArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = studentPollArray.toString().getBytes();
							VolleySingleton.getInstance(ChairmanStudentPoll.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
							studentPollListView.invalidateViews();
							studentPolllListAdapter = new StudentPolllListAdapter(ChairmanStudentPoll.this, studentPollArray);
							studentPollListView.setAdapter(studentPolllListAdapter);
							studentPolllListAdapter.notifyDataSetChanged();
							swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
						}
					}
					else
						Utils.showToast(ChairmanStudentPoll.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(ChairmanStudentPoll.this, "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(ChairmanStudentPoll.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(ChairmanStudentPoll.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}

	private void initData1()
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				studentPollArray= null;
			}
			else
			{
				studentPollArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentPollArray!= null)
		{
			studentPolllListAdapter= new StudentPolllListAdapter(ChairmanStudentPoll.this,studentPollArray);
			studentPollListView.setAdapter(studentPolllListAdapter);
			studentPolllListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentPollList1()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentPoll.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(ChairmanStudentPoll.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ChairmanStudentPoll.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("poll_List"))
							{
								studentPollArray= new JSONObject(response).getJSONArray("poll_List");
								if(null!=studentPollArray && studentPollArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentPollArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanStudentPoll.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
									studentPollListView.invalidateViews();
									studentPolllListAdapter = new StudentPolllListAdapter(ChairmanStudentPoll.this, studentPollArray);
									studentPollListView.setAdapter(studentPolllListAdapter);
									studentPolllListAdapter.notifyDataSetChanged();
									swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
								}
							}
							else
								Utils.showToast(ChairmanStudentPoll.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(ChairmanStudentPoll.this, "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(ChairmanStudentPoll.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(ChairmanStudentPoll.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}

	
}
