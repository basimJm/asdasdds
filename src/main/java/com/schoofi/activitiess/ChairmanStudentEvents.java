package com.schoofi.activitiess;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Cache;
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
import com.schoofi.adapters.TeacherEventListAdapter;
import com.schoofi.adapters.TeacherEventViewPagerAdapter;
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
import java.util.HashMap;
import java.util.Map;

import astuetz.PagerSlidingTabStrip;
import androidx.appcompat.widget.Toolbar;

public class ChairmanStudentEvents extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerListener;
    ActionBar actionBar;
    private ListView listView;
    private HomeUserAdapter homeUserAdapter;
    private FloatingActionButton fab;
    SwipyRefreshLayout swipyRefreshLayout;
    ArrayList<String> permissionArray1 = new ArrayList<String>();
    ListView chairmanStudentEventListView;
    TeacherEventListAdapter teacherEventListAdapter;
    private JSONArray teacherEventListArray;
    private JSONArray schoolNotificationListArray;
    String url1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
        getSupportActionBar().setTitle("Events");

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Student Events");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_student_events);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new TeacherEventViewPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        tabsStrip.setViewPager(viewPager);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        homeUserAdapter = new HomeUserAdapter(this,permissionArray1);
        listView.setAdapter(homeUserAdapter);
        listView.setOnItemClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerListener = new ActionBarDrawerToggle(ChairmanStudentEvents.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close)
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



        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChairmanStudentEvents.this, EventAdminUpload.class);
                startActivity(intent);
            }
        });

        drawerlayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(android.R.color.transparent);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        /*swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        chairmanStudentEventListView = (ListView) findViewById(R.id.listView_ch_events);

        initData();
        getChairmanStudentLeaveList();

        chairmanStudentEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChairmanStudentEvents.this,TeacherEventListDetails.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });*/

        /*swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getChairmanStudentLeaveList();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initData();
        //getChairmanStudentLeaveList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(ChairmanStudentEvents.this,ChairmanDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChairmanStudentEvents.this,ChairmanDashboard.class);
        startActivity(intent);
        this.finish();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if(Preferences.getInstance().userRoleId.matches("7"))
        {


            switch(position)
            {
                case 0 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;



                case 2:	if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 5 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 13:


                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 14:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 15:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 16:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 17:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 18:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 19:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

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

        else
        {
            switch(position)
            {
                case 0 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;



                case 2:	if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 5 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 13:


                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 14:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 15:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 16:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 17:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 18:

                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanStudentEvents.this, ChairmanYearlyPlanner.class);

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

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                teacherEventListArray= null;
            }
            else
            {
                teacherEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherEventListArray!= null)
        {
            teacherEventListAdapter= new TeacherEventListAdapter(ChairmanStudentEvents.this,teacherEventListArray,"2");
            chairmanStudentEventListView.setAdapter(teacherEventListAdapter);
            teacherEventListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentEvents.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                        Utils.showToast(ChairmanStudentEvents.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(ChairmanStudentEvents.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("events"))
                    {
                        teacherEventListArray= new JSONObject(response).getJSONArray("events");
                        if(null!=teacherEventListArray && teacherEventListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherEventListArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanStudentEvents.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
                            chairmanStudentEventListView.invalidateViews();
                            teacherEventListAdapter= new TeacherEventListAdapter(ChairmanStudentEvents.this,teacherEventListArray,"2");
                            chairmanStudentEventListView.setAdapter(teacherEventListAdapter);
                            teacherEventListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(ChairmanStudentEvents.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(ChairmanStudentEvents.this, "Error fetching modules! Please try after sometime.");
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
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(ChairmanStudentEvents.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                // params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(ChairmanStudentEvents.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(ChairmanStudentEvents.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
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
                        Intent intent = new Intent(ChairmanStudentEvents.this,AboutSchool.class);
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

                                Intent intent = new Intent(ChairmanStudentEvents.this, WebViewActivity.class);
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
}
