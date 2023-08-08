package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.HomeUserAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EventModel;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.SchoolPlannerListParsingMainVO;
import com.schoofi.utils.SchoolPlannerListParsingVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.memfis19.cadar.CalendarController;
import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.event.CalendarPrepareCallback;
import io.github.memfis19.cadar.event.DisplayEventCallback;
import io.github.memfis19.cadar.event.OnDayChangeListener;
import io.github.memfis19.cadar.event.OnEventClickListener;
import io.github.memfis19.cadar.event.OnMonthChangeListener;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.EventDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.MonthDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.WeekDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.EventDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.MonthDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.WeekDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.model.ListItemModel;
import io.github.memfis19.cadar.internal.utils.DateUtils;
import io.github.memfis19.cadar.settings.ListCalendarConfiguration;
import io.github.memfis19.cadar.settings.MonthCalendarConfiguration;
import io.github.memfis19.cadar.view.ListCalendar;
import io.github.memfis19.cadar.view.MonthCalendar;

import androidx.appcompat.widget.Toolbar;

public class ChairmanYearlyPlanner extends AppCompatActivity implements AdapterView.OnItemClickListener,CalendarPrepareCallback {

    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerListener;
    ActionBar actionBar;
    private ListView listView;
    private HomeUserAdapter homeUserAdapter;
    private FloatingActionButton fab;
    SwipyRefreshLayout swipyRefreshLayout;
    ArrayList<String> permissionArray1 = new ArrayList<String>();
    private MonthCalendar monthCalendar;
    private ListCalendar listCalendar;

    private List<Event> events = new ArrayList<>();
    Event event;
    private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
    private ArrayList<SchoolPlannerListParsingMainVO> schoolPlannerListParsingMainVOs;
    private ArrayList<SchoolPlannerListParsingVO> schoolPlannerListParsingVOs;
    private JSONArray schoolPlannerListArray;
    JSONArray jsonArray;
    JSONObject jsonObject,jsonObject1;

    private String value;
    private JSONArray schoolNotificationListArray;
    String url1;
    String v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
        getSupportActionBar().setTitle("Yearly Planner");

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Yearly Planner");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_chairman_yearly_planner);

        postLocalRegistration();






        // events.add(new EventModel("llll"));

        monthCalendar = (MonthCalendar) findViewById(R.id.month_calendar);
        listCalendar = (ListCalendar) findViewById(R.id.listCalendar);





        EventDecoratorFactory eventDecoratorFactory = new EventDecoratorFactory() {
            @Override
            public EventDecorator createEventDecorator(View parent) {
                return new EventDecoratorImpl(parent);
            }
        };

        WeekDecoratorFactory weekDecoratorFactory = new WeekDecoratorFactory() {
            @Override
            public WeekDecorator createWeekDecorator(View parent) {
                return new WeeDecoratorImpl(parent);
            }
        };

        MonthDecoratorFactory monthDecoratorFactory = new MonthDecoratorFactory() {
            @Override
            public MonthDecorator createMonthDecorator(View parent) {
                return new MonthDecoratorImpl(parent);
            }
        };

        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        homeUserAdapter = new HomeUserAdapter(this,permissionArray1);
        listView.setAdapter(homeUserAdapter);
        listView.setOnItemClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerListener = new ActionBarDrawerToggle(ChairmanYearlyPlanner.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close)
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
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChairmanYearlyPlanner.this, AddPlannerEvent.class);
                startActivity(intent);
            }
        });



            fab.setVisibility(View.INVISIBLE);


        drawerlayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(android.R.color.transparent);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        monthCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                listCalendar.setSelectedDay(DateUtils.setTimeToMidnight((Calendar) calendar.clone()), false);
                Log.d("ll", String.valueOf(calendar.getTime()));

                /*Intent intent = new Intent(SchoolPlannerMakingFirstScreen.this,AddPlannerEvent.class);
                startActivity(intent);*/
            }
        });
        monthCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                listCalendar.setSelectedDay(DateUtils.setTimeToMonthStart((Calendar) calendar.clone()), false);


            }
        });

        listCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                monthCalendar.setSelectedDay(calendar, false);
            }
        });

        listCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                monthCalendar.setSelectedDay(calendar, true);
            }
        });

        listCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onEventClick(Event event, int position) {
                Log.i("onEventClick", event.getEventTitle());
                Intent intent = new Intent(ChairmanYearlyPlanner.this,SchoolPlannerEventDetail.class);
                intent.putExtra("title",event.getEventTitle());
                intent.putExtra("location",event.getLocation());
                intent.putExtra("description",event.getEventDescription());
                intent.putExtra("fromDate",String.valueOf(event.getOriginalEventStartDate()));
                intent.putExtra("toDate",String.valueOf(event.getEventEndDate()));
                intent.putExtra("classList",event.getClassName());
                intent.putExtra("plannerId",event.getPlannerID());
                intent.putExtra("isAllday",event.isAllDay1());
                intent.putExtra("isHoliday",event.isHoliday());
                intent.putExtra("sectionId",event.getSectionId());

                startActivity(intent);
            }

            @Override
            public void onSyncClick(Event event, int position) {
                Log.i("onSyncClick", String.valueOf(event));
            }
        });
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
    public void onCalendarReady(CalendarController calendar) {
        if (calendar == monthCalendar) {
            monthCalendar.displayEvents(events, new DisplayEventCallback() {
                @Override
                public void onEventsDisplayed() {

                }
            });
        } else if (calendar == listCalendar) {
            listCalendar.displayEvents(events, new DisplayEventCallback() {
                @Override
                public void onEventsDisplayed() {

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
          if(v.matches("1")) {
              monthCalendar.releaseCalendar();
              listCalendar.releaseCalendar();
          }

          else
          {

          }
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }


                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;



                case 2:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanYearlyPlanner.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }



                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 5 : if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {

                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {

                    break;
                }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 13:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 14:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 15:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 16:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 17:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 18:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;



                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;



                case 2:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 5 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 13:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 14:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 15:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;

                case 16:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {

                        break;
                    }

                    break;


                case 17:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanYearlyPlanner.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
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
                        Intent intent = new Intent(ChairmanYearlyPlanner.this,AboutSchool.class);
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

                                Intent intent = new Intent(ChairmanYearlyPlanner.this, WebViewActivity.class);
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

    protected void postLocalRegistration()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        Preferences.getInstance().loadPreference(getApplicationContext());

        if(Preferences.getInstance().userRoleId.matches("3"))
        {
            value = "2";
        }

        else
        {
            value="1";
        }


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PLANNER_LIST + "?token=" + Preferences.getInstance().token + "&device_id="+Preferences.getInstance().deviceId+"&value="+value+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId;

        StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                System.out.println(url1);


                try {
                    responseObject = new JSONObject(response);
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        v="0";
                        loading.dismiss();
                    }
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        loading.dismiss();
                        v="0";
                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();
                        v="1";

                        schoolPlannerListArray = new JSONObject(response).getJSONArray("responseObject");
                        // profileUrl = schoolDiaryArray.getJSONObject(0).getString("school_banner");



                        if(null!=schoolPlannerListArray && schoolPlannerListArray.length()>=0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolPlannerListArray.toString().getBytes();
                            schoolPlannerListParsingMainVOs = new ArrayList<SchoolPlannerListParsingMainVO>();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PLANNER_LIST + "?token=" + Preferences.getInstance().token + "&device_id="+Preferences.getInstance().deviceId+"&value="+value+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId, e);



                            for(int i=0;i<schoolPlannerListArray.length();i++)
                            {
                                SchoolPlannerListParsingMainVO schoolPlannerListParsingMainVO = new SchoolPlannerListParsingMainVO();
                                schoolPlannerListParsingMainVO.setDate(schoolPlannerListArray.getJSONObject(i).getString("date_scope"));
                                jsonObject = schoolPlannerListArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                schoolPlannerListParsingVOs= new ArrayList<SchoolPlannerListParsingVO>();
                                //Utils.showToast(getApplicationContext(),jsonArray.toString());
                                //Log.d("fff",jsonArray.toString());

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    SchoolPlannerListParsingVO schoolPlannerListParsingVO = new SchoolPlannerListParsingVO();
                                    schoolPlannerListParsingVO.setAllDay(jsonObject1.getString("is_allday"));
                                    schoolPlannerListParsingVO.setHoliday(jsonObject1.getString("is_holiday"));
                                    schoolPlannerListParsingVO.setStartDate(jsonObject1.getString("from_date"));
                                    schoolPlannerListParsingVO.setEndDate(jsonObject1.getString("to_date"));
                                    schoolPlannerListParsingVO.setStartTime(jsonObject1.getString("start_time"));
                                    schoolPlannerListParsingVO.setEndTime(jsonObject1.getString("end_time"));
                                    schoolPlannerListParsingVO.setTitle(jsonObject1.getString("title"));
                                    schoolPlannerListParsingVO.setLocation(jsonObject1.getString("location"));
                                    schoolPlannerListParsingVO.setDetails(jsonObject1.getString("details"));
                                    schoolPlannerListParsingVO.setPlannerId(jsonObject1.getString("planner_id"));
                                    schoolPlannerListParsingVO.setClassName(jsonObject1.getString("audience_name"));
                                    schoolPlannerListParsingVO.setSectionId(jsonObject1.getString("audience_section"));

                                    String demo = schoolPlannerListParsingMainVO.getDate()+" "+schoolPlannerListParsingVO.getStartTime();
                                    String demo1 = schoolPlannerListParsingVO.getEndDate()+" "+schoolPlannerListParsingVO.getEndTime();
                                    String demo2 = schoolPlannerListParsingVO.getStartDate()+" "+schoolPlannerListParsingVO.getStartTime();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    Date date3 = null;

                                    Date date4=null;

                                    Date date5=null;

                                    Date date6 = null;
                                    Date date9=null;
                                    Date date10=null;

                                    try {
                                        date3 = formatter.parse(demo);
                                        date5 = formatter.parse(demo1);
                                        date9 = formatter.parse(demo2);
                                    } catch (ParseException e3) {
                                        // TODO Auto-generated catch block
                                        e3.printStackTrace();
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                                    String date2="";
                                    String date7="";
                                    String date8="";
                                    date2 = sdf.format(date3);
                                    date7 = sdf.format(date5);
                                    date8 = sdf.format(date9);

                                    try {
                                        date4 = sdf.parse(date2);
                                        date6 = sdf.parse(date7);
                                        date10 = sdf.parse(date8);
                                    } catch (ParseException e3) {
                                        // TODO Auto-generated catch block
                                        e3.printStackTrace();
                                    }

                                    Boolean allDay;

                                    if(schoolPlannerListParsingVO.getAllDay().matches("yes"))
                                    {
                                        allDay = true;
                                    }

                                    else
                                    {
                                        allDay=false;
                                    }

                                    events.add(new EventModel(schoolPlannerListParsingVO.getTitle(),schoolPlannerListParsingVO.getLocation(),date4,date6,date10,schoolPlannerListParsingVO.getPlannerId(),schoolPlannerListParsingVO.getClassName(),schoolPlannerListParsingVO.getDetails(),allDay,schoolPlannerListParsingVO.getHoliday(),schoolPlannerListParsingVO.getSectionId(),schoolPlannerListParsingVO.getAllDay()));
                                    // events.add(new EventModel());




                                    //Log.d("kkk",date2);



                                    schoolPlannerListParsingVOs.add(schoolPlannerListParsingVO);
                                    // Utils.showToast(getApplicationContext(),"kkk");

                                }

                                schoolPlannerListParsingMainVO.setItems(schoolPlannerListParsingVOs);
                                schoolPlannerListParsingMainVOs.add(schoolPlannerListParsingMainVO);



                                //Date originalStartDate = new Date();

                                // Log.d("date", String.valueOf(originalStartDate));


                            }

                            MonthCalendarConfiguration.Builder builder = new MonthCalendarConfiguration.Builder(ChairmanYearlyPlanner.this);
                            ListCalendarConfiguration.Builder listBuilder = new ListCalendarConfiguration.Builder(ChairmanYearlyPlanner.this);

                            monthCalendar.setCalendarPrepareCallback(ChairmanYearlyPlanner.this);
                            listCalendar.setCalendarPrepareCallback(ChairmanYearlyPlanner.this);

                            monthCalendar.prepareCalendar(builder.build());
                            listCalendar.prepareCalendar(listBuilder.build());


                        }
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong!!!", Toast.LENGTH_LONG).show();
                    setProgressBarIndeterminateVisibility(false);
                    loading.dismiss();
                }


                // mParties = new String[]
                // dynamicToolbarColor();


                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("value",value);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("cls_id",Preferences.getInstance().studentClassId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                /*if(diaryModuleId.matches("") || diaryModuleId.matches("null"))
                {
                    params.put("diary_module_id","A");
                }
                else {
                    params.put("diary_module_id", diaryModuleId);
                }

                if(subjectId.matches("") || subjectId.matches("null"))
                {
                    params.put("subject_id","A");
                }
                else {
                    params.put("subject_id", subjectId);
                }

                if(fromDate.matches("") || fromDate.matches("null"))
                {
                    params.put("from_date","");
                }

                else {
                    params.put("from_date", fromDate);
                }

                if(toDate.matches("") || toDate.matches("null"))
                {
                    params.put("to_date","");
                }

                else {
                    params.put("to_date", toDate);
                }

                if(teacherId.matches("") || teacherId.matches("null"))
                {
                    params.put("teacher_id",teacherId);
                }

                else {
                    params.put("teacher_id", teacherId);
                }*/
                //System.out.print(Preferences.getInstance().userId);




                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }

    private class EventDecoratorImpl implements EventDecorator {

        private TextView day,month,title,location;

        public EventDecoratorImpl(View parent) {
            day = (TextView) parent.findViewById(R.id.text_date);
            month = (TextView) parent.findViewById(R.id.text_month);
            title = (TextView) parent.findViewById(R.id.textView_student_announcement_title);
            location = (TextView) parent.findViewById(R.id.textView_student_announcement_person);

        }

        @Override
        public void onBindEventView(View view, Event event, ListItemModel previous, int position) {
            view.setBackgroundColor(ContextCompat.getColor(ChairmanYearlyPlanner.this, R.color.eventBackground));
            day.setText("kjh");
            month.setText("kkk");
            title.setText("mmm");
            location.setText("llk");

        }
    }

    private class MonthDecoratorImpl implements MonthDecorator {

        private ImageView monthBackground;
        private TextView monthTitle;
        private Custom custom;

        public MonthDecoratorImpl(View parent) {
            monthBackground = (ImageView) parent.findViewById(R.id.month_background);
            monthTitle = (TextView) parent.findViewById(R.id.month_label);
        }

        @Override
        public void onBindMonthView(View view, Calendar month) {
            monthBackground.setImageDrawable(null);

            final int backgroundId = getBackgroundId(month.get(Calendar.MONTH));
            monthTitle.setText(month.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            Picasso.with(monthTitle.getContext().getApplicationContext()).load(backgroundId).into(monthBackground, new Callback() {
                @Override
                public void onSuccess() {
                    if (Build.VERSION.SDK_INT > 13) {
                        monthBackground.setScrollX(0);
                        monthBackground.setScrollY(0);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.OnScrollListener getScrollListener() {
            custom = new Custom();
            return custom;
        }
    }

    private class WeeDecoratorImpl implements WeekDecorator {

        private TextView title;

        public WeeDecoratorImpl(View parent) {
            title = (TextView) parent.findViewById(io.github.memfis19.cadar.R.id.week_title);
        }

        @Override
        public void onBindWeekView(View view, Pair<Calendar, Calendar> period) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(title.getContext().getString(io.github.memfis19.cadar.R.string.calendar_week));
            stringBuilder.append("custom ");
            stringBuilder.append(period.first.get(Calendar.WEEK_OF_YEAR));
            stringBuilder.append(", ");
            stringBuilder.append(DateFormat.format("dd MMM", period.first));
            stringBuilder.append(" - ");
            stringBuilder.append(DateFormat.format("dd MMM", period.second));

            final Spannable date = new SpannableString(stringBuilder.toString());

            title.setText(date);
        }
    }

    private int getBackgroundId(int month) {
        int backgroundId = io.github.memfis19.cadar.R.drawable.bkg_12_december;

        if (month == Calendar.JANUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_01_january;
        } else if (month == Calendar.FEBRUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_02_february;
        } else if (month == Calendar.MARCH) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_03_march;
        } else if (month == Calendar.APRIL) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_04_april;
        } else if (month == Calendar.MAY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_05_may;
        } else if (month == Calendar.JUNE) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_06_june;
        } else if (month == Calendar.JULY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_07_july;
        } else if (month == Calendar.AUGUST) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_08_august;
        } else if (month == Calendar.SEPTEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_09_september;
        } else if (month == Calendar.OCTOBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_10_october;
        } else if (month == Calendar.NOVEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_11_november;
        }

        return backgroundId;
    }

    private class Custom extends RecyclerView.OnScrollListener {

        private View monthBackground;

        Custom() {
        }

        public void setMonthBackground(View monthBackground) {
            this.monthBackground = monthBackground;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (monthBackground != null) monthBackground.scrollBy(dx, (-1) * (dy / 10));
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(ChairmanYearlyPlanner.this,ChairmanDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChairmanYearlyPlanner.this,ChairmanDashboard.class);
        startActivity(intent);
        this.finish();
    }
}
