package com.schoofi.activitiess;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.schoofi.adapters.AdminBusListViewAdapter;
import com.schoofi.adapters.ChairmanStudentAttendanceAdapter;
import com.schoofi.adapters.HomeUserAdapter;
import com.schoofi.adapters.TeacherStudentBusAttendanceBusListAdapter;
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

public class ChairmanBusListScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerListener;
    ActionBar actionBar;
    private ListView listView;
    private HomeUserAdapter homeUserAdapter;
    ImageView back;
    ArrayList<String> permissionArray1 = new ArrayList<String>();
    private SwipyRefreshLayout swipyRefreshLayout;
    private ListView busListView;
    private JSONArray adminBusListArray;
    private Button done;
    public ArrayList attendance = new ArrayList();
    public ArrayList attendance1 = new ArrayList();
    AdminBusListViewAdapter adminBusListViewAdapter;
    String array1,array2;
    String array3,array4;
    int i=0;
    public boolean c;
    private JSONArray schoolNotificationListArray;
    String url1;

    TeacherStudentBusAttendanceBusListAdapter teacherStudentBusAttendanceBusListAdapter;
    private Button allBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
        getSupportActionBar().setTitle("Bus Tracking");

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Bus List Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_bus_list_screen);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        Preferences.getInstance().loadPreference(getApplicationContext());

        allBuses = (Button) findViewById(R.id.btn_all_busses);
        allBuses.setVisibility(View.GONE);

        Preferences.getInstance().loadPreference(getApplicationContext());

        if(Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8"))
        {
            allBuses.setVisibility(View.VISIBLE);
        }

        else
        {
            allBuses.setVisibility(View.GONE);
        }

        allBuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanBusListScreen.this,AllBussesMapActivity.class);
                startActivity(intent);
            }
        });

        homeUserAdapter = new HomeUserAdapter(this,permissionArray1);
        listView.setAdapter(homeUserAdapter);
        listView.setOnItemClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerListener = new ActionBarDrawerToggle(ChairmanBusListScreen.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close)
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
        //drawerlayout.openDrawer(listView);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        busListView = (ListView) findViewById(R.id.listViewAdminBusListView);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getChairmanStudentLeaveList();

            }
        });

       busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               try {
                   Intent intent = new Intent(ChairmanBusListScreen.this,BusAdminShiftScreen.class);
                   intent.putExtra("bus_no",adminBusListArray.getJSONObject(position).getString("bus_number"));
                   intent.putExtra("value","1");
                   startActivity(intent);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });


        initData();
        getChairmanStudentLeaveList();

        done = (Button) findViewById(R.id.btn_done);

        done.setVisibility(View.INVISIBLE);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attendance = new ArrayList<String>();

                for(int k =0;k<i;k++)
                {
                    try {
                        System.out.println("array" +adminBusListViewAdapter.adminBusListArray.toString());
                        if(adminBusListViewAdapter.adminBusListArray.getJSONObject(k).getString("maps").toString().matches("P")) {
                            attendance1.add(adminBusListViewAdapter.adminBusListArray.getJSONObject(k).getString("route_no"));
                            attendance.add(adminBusListViewAdapter.adminBusListArray.getJSONObject(k).getString("bus_number"));
                        }

                        else
                        {
                            Log.d("kkk","kkk");
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                array1 = attendance.toString();
                array2 = array1.substring(1, array1.length()-1);
                array3 = attendance1.toString();
                array4 = array3.substring(1, array3.length()-1);
                //Utils.showToast(getApplicationContext(),array2.toString());

                Intent intent = new Intent(ChairmanBusListScreen.this,AboutSchoolContactUs.class);
                intent.putExtra("array",array2);
                intent.putExtra("array1",array4);
                startActivity(intent);

            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(ChairmanBusListScreen.this,ChairmanDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChairmanBusListScreen.this,ChairmanDashboard.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;



                case 2:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 5 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                    Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }
                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }
                    break;

                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 13:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 14:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 15:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 16:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 17:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 18:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 19:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;



                case 2:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 5 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                    Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 13:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 14:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 15:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 16:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 17:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 18:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        /*Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
                        Intent intent = new Intent(ChairmanBusListScreen.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanBusListScreen.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                adminBusListArray= null;
            }
            else
            {
                adminBusListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminBusListArray!= null)
        {

            teacherStudentBusAttendanceBusListAdapter = new TeacherStudentBusAttendanceBusListAdapter(ChairmanBusListScreen.this, adminBusListArray);
            busListView.setAdapter(teacherStudentBusAttendanceBusListAdapter);
            teacherStudentBusAttendanceBusListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanBusListScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST;//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
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
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("VehicleList"))
                    {
                        adminBusListArray= new JSONObject(response).getJSONArray("VehicleList");


                        if(null!=adminBusListArray && adminBusListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminBusListArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanBusListScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
                            busListView.invalidateViews();

                            teacherStudentBusAttendanceBusListAdapter = new TeacherStudentBusAttendanceBusListAdapter(ChairmanBusListScreen.this, adminBusListArray);
                            busListView.setAdapter(teacherStudentBusAttendanceBusListAdapter);
                            teacherStudentBusAttendanceBusListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
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
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();;
        getChairmanStudentLeaveList();
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
                        Intent intent = new Intent(ChairmanBusListScreen.this,AboutSchool.class);
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

                                Intent intent = new Intent(ChairmanBusListScreen.this, WebViewActivity.class);
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
