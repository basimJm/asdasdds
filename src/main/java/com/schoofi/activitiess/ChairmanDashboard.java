package com.schoofi.activitiess;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.ChairmanDashboardHorizontalListviewAdapter;
import com.schoofi.adapters.ChairmanDashboardListviewAdapter;
import com.schoofi.adapters.ChairmanStudentLeaveAdapter;
import com.schoofi.adapters.HomeUserAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ChairmanDashBoardChildListViewVO;
import com.schoofi.utils.ChairmanDashboardListViewVO;
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
import androidx.appcompat.widget.Toolbar;

public class ChairmanDashboard extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView screenTitle;

    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerListener;
    ActionBar actionBar;
    JSONArray  categoryWise;

    private HomeUserAdapter homeUserAdapter;
    private JSONArray chairmanDashBoardArray,chairmanDashBoardArray1;
    private TextView viewAnalysis7;
    private ListView listView,litViewResults;
    private TextView totalTeachers,maleteachers,femaleteachers,viewAll,viewClassAnalysis,totalVisitors,dailyVisitors,weeklyVisitors,viewAll1,viewAccessPointsAnalysis,textTotalAccessPointsAnalysis,totalEmployeesAttendanceTaken,totalEmployeesPresent,totalEmployeesAbsent,totalEmployeeLeaves;

    private TextView totalNewAdmissions,totalNewBoysAdmissions,totalNewGirlsAdmissions,totalNewAdmissions1,totalNewBoysAdmissions1,totalNewGirlsAdmissions1,totalStudents,noOfBoys,noOfGirls,totalAttendanceTaken,totalPresent,totalAbsent,totalFeeAmount,totalFeeDue,totalFeeNotYetDue,totalFeePaid,totalPendingFee,studentTotalExams;

    ArrayList<String> permissionArray1 = new ArrayList<String>();
    ArrayList<ChairmanDashboardListViewVO> temparr;
    ArrayList<ChairmanDashBoardChildListViewVO> chairmanDashBoardChildListViewVOs;
    ChairmanDashboardListviewAdapter chairmanDashboardListviewAdapter;
    private TextView viewAnalysis,employeeViewAnalysis,newAddmissionAnalysis,newAddmissionAnalysis1;
    JSONObject jsonObject,jsonObject1;
    private JSONArray schoolNotificationListArray;
    String url1;
    private TextView viewAnalysis5,viewMedicalRecords,dailyCollections,monthlyCollections,weeklyCollections,viewAnalysis1,totalAssignmentsUploaded,todayAssignment,weeklyAssignment,viewAnalysis2,totalFeesCollections,totalDiscount,totalDiaryUploads,totalDailyDiaryUploads,totalWeeklyDiaryUploads,viewDiaryAnalysis;
    String Rs;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();

        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
        getSupportActionBar().setTitle("Dashboard");

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Dashboard");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_chairman_dashboard);


        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        listView = (ListView) findViewById(R.id.drawerList);
        homeUserAdapter = new HomeUserAdapter(this,permissionArray1);
        listView.setAdapter(homeUserAdapter);
        listView.setOnItemClickListener(this);
        viewAll = (TextView) findViewById(R.id.text_total_view_analysis3);
        totalStudents = (TextView) findViewById(R.id.text_total_students);
        noOfBoys = (TextView) findViewById(R.id.text_total_boys);
        noOfGirls = (TextView) findViewById(R.id.text_total_girls);
        totalAttendanceTaken = (TextView) findViewById(R.id.text_total_attendance_taken);
        totalPresent = (TextView) findViewById(R.id.text_total_present);
        totalAbsent = (TextView) findViewById(R.id.text_total_absent);
        totalFeeAmount = (TextView) findViewById(R.id.text_total_fee_net_amount);
        totalFeesCollections = (TextView) findViewById(R.id.total_fees_collection);
        totalDiscount = (TextView) findViewById(R.id.text_total_fee_discount_amount);
        totalFeeDue = (TextView) findViewById(R.id.text_total_fee_due1);
        totalNewAdmissions = (TextView) findViewById(R.id.text_total_new_admissions);
        totalNewBoysAdmissions = (TextView) findViewById(R.id.text_total_new_boys);
        totalNewGirlsAdmissions = (TextView) findViewById(R.id.text_total_new_girls);

        totalNewAdmissions1 = (TextView) findViewById(R.id.text_total_new_admissions1);
        totalNewBoysAdmissions1 = (TextView) findViewById(R.id.text_total_new_boys1);
        totalNewGirlsAdmissions1 = (TextView) findViewById(R.id.text_total_new_girls1);
        totalFeeNotYetDue = (TextView) findViewById(R.id.text_total_fee_not_yet_due1);
        totalFeePaid = (TextView) findViewById(R.id.text_total_fee_paid1);
        totalPendingFee = (TextView) findViewById(R.id.text_total_pending_fee1);
        dailyCollections = (TextView) findViewById(R.id.text_total_daily_collections);
        weeklyCollections = (TextView) findViewById(R.id.text_total_weekly_collections1);
        monthlyCollections = (TextView) findViewById(R.id.text_total_monthly_collections1);
        viewAnalysis1 = (TextView) findViewById(R.id.text_total_view_analysis1);
        totalTeachers = (TextView) findViewById(R.id.text_total_teachers);
        maleteachers = (TextView) findViewById(R.id.text_total_males);
        femaleteachers = (TextView) findViewById(R.id.text_total_females);
        newAddmissionAnalysis = (TextView) findViewById(R.id.text_total_view_analysis13);
        newAddmissionAnalysis1 = (TextView) findViewById(R.id.text_total_view_analysis113);
        viewAccessPointsAnalysis =(TextView)findViewById(R.id.text_total_view_analysis6);
        textTotalAccessPointsAnalysis = (TextView) findViewById(R.id.text_total__access_points);
        viewAnalysis7= (TextView) findViewById(R.id.text_view_analysis7);
        viewMedicalRecords = (TextView) findViewById(R.id.text_view_medical_analysis);
        viewClassAnalysis = (TextView) findViewById(R.id.text_view_class_wise_analysis);
        totalEmployeesAttendanceTaken = (TextView) findViewById(R.id.text_total_employee_attendance_taken);
        totalEmployeesAbsent = (TextView) findViewById(R.id.text_total_employee_absent);
        totalEmployeesPresent = (TextView) findViewById(R.id.text_total_employee_present);
        totalEmployeeLeaves = (TextView) findViewById(R.id.text_total_employee_leave);
        employeeViewAnalysis = (TextView) findViewById(R.id.text_total_employee_view_analysis);
        totalDiaryUploads = (TextView) findViewById(R.id.text_total_diary_uploaded);
        totalWeeklyDiaryUploads = (TextView) findViewById(R.id.text_diary_weekly);
        totalDailyDiaryUploads = (TextView) findViewById(R.id.text_diary_today);
        viewDiaryAnalysis = (TextView) findViewById(R.id.text_diary_view_analysis2);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        viewDiaryAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanDashboard.this,AdminClassListScreen.class);
                intent.putExtra("value3","9");
                startActivity(intent);
            }
        });



        //litViewResults = (ListView) findViewById(R.id.listView_results);
       // studentTotalExams = (TextView) findViewById(R.id.text_total_exams);

        employeeViewAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanDashboard.this,ChairmanEmployeeDepartmentWiseCount.class);
                intent.putExtra("value","0");
                startActivity(intent);

            }
        });

        viewMedicalRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanDashboard.this,AdminHealthCard.class);
                startActivity(intent);
            }
        });

        totalDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChairmanDashboard.this,ChairmanDiscountAnalysisScreen.class);
                startActivity(intent);


            }
        });

        viewClassAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(ChairmanDashboard.this,AdminClassListScreen.class);
                intent5.putExtra("value3","0");
                startActivity(intent5);

            }
        });

        newAddmissionAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(ChairmanDashboard.this,NewAdmissionClassWise.class);
                    intent.putExtra("value3","0");
                    intent.putExtra("session",chairmanDashBoardArray.getJSONObject(0).getString("crr_session"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        newAddmissionAnalysis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(ChairmanDashboard.this,NewAdmissionClassWise.class);
                    intent.putExtra("value3","0");
                    intent.putExtra("session",chairmanDashBoardArray.getJSONObject(0).getString("upcoming_session"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });




        viewAnalysis7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChairmanDashboard.this,StudentAnalysis.class);
                startActivity(intent);
            }
        });

        totalAssignmentsUploaded = (TextView) findViewById(R.id.text_total_assignments_uploaded);
        todayAssignment = (TextView) findViewById(R.id.text_today);
        weeklyAssignment = (TextView) findViewById(R.id.text_total_weekly);
        totalVisitors = (TextView) findViewById(R.id.text_total__visitor);
        dailyVisitors = (TextView) findViewById(R.id.text_total_visitor_daily);
        weeklyVisitors = (TextView) findViewById(R.id.text_total_visitor_weekly);
        viewAll1 = (TextView) findViewById(R.id.text_total_view_analysis4);
        viewAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanDashboard.this,AdminVisitorAnalysisFirstScreen.class);
                startActivity(intent);
            }
        });
        viewAnalysis2 = (TextView) findViewById(R.id.text_total_view_analysis2);

        viewAnalysis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChairmanDashboard.this,TeacherCoordinaterAssignmentAnalysisHomeScreen.class);
                startActivity(intent);

            }
        });

        viewAnalysis5= (TextView) findViewById(R.id.text_total_view_analysis5);

        viewAnalysis5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanDashboard.this,TeacherCoordinatorPendingFeesClassWise.class);
                startActivity(intent);
            }
        });

        viewAccessPointsAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanDashboard.this,TurnstileLogsMainScreen.class);
                startActivity(intent);
            }
        });



        viewAnalysis = (TextView) findViewById(R.id.text_total_view_analysis);

        viewAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Preferences.getInstance().schoolType.matches("College"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this,CollegeTeacherList.class);
                    startActivity(intent);
                }

                else {

                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanAttendanceDetailsScreen.class);
                    startActivity(intent);
                }
            }
        });

        viewAnalysis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChairmanDashboard.this,ChairmanFeesAnalysis.class);
                intent.putExtra("value","1");
                startActivity(intent);
            }
        });


        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChairmanDashboard.this,AdminEmployeeList.class);
                startActivity(intent);
            }
        });

        temparr = new ArrayList<ChairmanDashboardListViewVO>();
        ChairmanDashboardListViewVO chairmanDashboardListViewVO = new ChairmanDashboardListViewVO(">85(A)","71-85(B)","55-70(C)","41-54(D)","<40(E)");
        temparr.add(chairmanDashboardListViewVO);

        Toolbar toolbar = findViewById(R.id.toolbar);

        drawerListener = new ActionBarDrawerToggle(ChairmanDashboard.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close)
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

        Rs = getApplicationContext().getString(R.string.Rs);
        initData();
        getChairmanStudentLeaveList();

       /* litViewResults.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        //drawerlayout.openDrawer(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getChairmanStudentLeaveList();
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))

                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }


                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }



                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                       /* Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;



                case 2:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();
*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 5 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();*/

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 13:if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();*/

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {

                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 14:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 15:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 16:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 17:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 18:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 19:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

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
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }


                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 1:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;



                case 2:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();
*/
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 3 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 4 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 5 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();*/

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {

                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 6 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 7 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 8 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 9 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 10 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 11 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 12 :
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 13:if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                {
                    /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                    startActivity(intent);
                    finish();*/

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                    startActivity(intent);
                    finish();

                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                    Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                    Preferences.getInstance().isLoggedIn = false;
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                else
                if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                {
                    Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                    startActivity(intent);
                    finish();
                    break;
                }

                    break;

                case 14:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 15:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 16:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;


                case 17:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, HealthAndAuditHomeScreen.class);

                        startActivity(intent);
                        finish();
                        break;
                    }

                    break;

                case 18:
                    if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanHomeScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanSchoolAttendance.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
                    {
                        /*Intent intent = new Intent(ChairmanDashboard.this, ChairmanDashboard.class);
                        startActivity(intent);
                        finish();*/

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
                    {

                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentLeaves.class);
                        startActivity(intent);
                        finish();

                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentAnnouncement.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, NotificationIntentClass.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
                        Intent intent = new Intent(ChairmanDashboard.this, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ContactUs.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, LoginScreen.class);
                        Preferences.getInstance().isLoggedIn = false;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        startActivity(intent);
                        finish();
                        break;
                    }

                    else
                    if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ChairmanDashboard.this, ChairmanYearlyPlanner.class);

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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_URL+"?school_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1);
            if(e == null)
            {
                chairmanDashBoardArray= null;
            }
            else
            {
                chairmanDashBoardArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanDashBoardArray!= null)
        {
            /*chairmanStudentLeaveAdapter= new ChairmanStudentLeaveAdapter(ChairmanStudentLeaves.this,chairmanDashBoardArray);
            chairmanStudentLeaveListView.setAdapter(chairmanStudentLeaveAdapter);
            chairmanStudentLeaveAdapter.notifyDataSetChanged();*/

            try {
                totalStudents.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_student"));
                noOfBoys.setText("Boys "+chairmanDashBoardArray.getJSONObject(0).getString("total_boys"));
                noOfGirls.setText("Girls "+chairmanDashBoardArray.getJSONObject(0).getString("total_girls"));
                totalAttendanceTaken.setText(chairmanDashBoardArray.getJSONObject(0).getString("attendence_taken"));
                totalPresent.setText("Present "+chairmanDashBoardArray.getJSONObject(0).getString("attendence_present"));
                totalAbsent.setText("Absent "+chairmanDashBoardArray.getJSONObject(0).getString("attendence_absent"));
                totalFeeAmount.setText("Net Fee Amount: "+Rs+chairmanDashBoardArray.getJSONObject(0).getString("net_fee_amount")+"/-");
                totalDiscount.setText("Discount: "+Rs+chairmanDashBoardArray.getJSONObject(0).getString("discount_amount")+"/-");
                totalFeesCollections.setText("Total Fees: "+Rs+chairmanDashBoardArray.getJSONObject(0).getString("total_fee")+"/-");
                totalPendingFee.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("fee_unpaid")+"/-");
                totalFeePaid.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("fee_paid")+"/-");
                totalFeeDue.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("other_charges")+"/-");
                totalFeeNotYetDue.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("other_charges")+"/-");
                dailyCollections.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("daily_collection")+"/-");
                weeklyCollections.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("weekly_collection")+"/-");
                monthlyCollections.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("monthly_collection")+"/-");
                totalAssignmentsUploaded.setText("Total "+chairmanDashBoardArray.getJSONObject(0).getString("total_asn"));
                todayAssignment.setText("Today "+chairmanDashBoardArray.getJSONObject(0).getString("today_asn"));
                weeklyAssignment.setText("Weekly "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_asn"));

                totalTeachers.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_teachers"));
                femaleteachers.setText("Female: "+chairmanDashBoardArray.getJSONObject(0).getString("total_female_teachers"));
                maleteachers.setText("Male: "+chairmanDashBoardArray.getJSONObject(0).getString("total_male_teachers"));
                totalVisitors.setText("Total: "+chairmanDashBoardArray.getJSONObject(0).getString("total_visitors"));
                dailyVisitors.setText("Today: "+chairmanDashBoardArray.getJSONObject(0).getString("today_visitors"));
                weeklyVisitors.setText("Weekly: "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_visitors"));
                textTotalAccessPointsAnalysis.setText("Total: "+ chairmanDashBoardArray.getJSONObject(0).getString("total_access_points"));
                totalEmployeesAttendanceTaken.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_employee_attendance"));
                totalEmployeesPresent.setText("Present "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_present_attendance"));
                totalEmployeesAbsent.setText("Absent "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_absent_attendance"));
                totalEmployeeLeaves.setText("Leave "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_leave_attendance"));
                totalNewAdmissions.setText("Session: "+chairmanDashBoardArray.getJSONObject(0).getString("crr_session")+"    -    "+chairmanDashBoardArray.getJSONObject(0).getString("crr_admissions_new"));
                totalNewBoysAdmissions.setText("Boys "+chairmanDashBoardArray.getJSONObject(0).getString("crr_admissions_boys"));
                totalNewGirlsAdmissions.setText("Girls "+chairmanDashBoardArray.getJSONObject(0).getString("crr_admissions_girls"));
                totalNewAdmissions1.setText("Session: "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_session")+"    -    "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_admissions_new"));
                totalNewBoysAdmissions1.setText("Boys "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_admissions_boys"));
                totalNewGirlsAdmissions1.setText("Girls "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_admissions_girls"));

                if(chairmanDashBoardArray.getJSONObject(0).getString("student_type_data").matches("") || chairmanDashBoardArray.getJSONObject(0).getString("student_type_data").matches("null"))
                {

                }
                else {

                    Log.d("kkk","poo");

                    categoryWise = new JSONArray(chairmanDashBoardArray.getJSONObject(0).getString("student_type_data"));


                    mAdapter = new ChairmanDashboardHorizontalListviewAdapter(ChairmanDashboard.this, categoryWise);
                    mRecyclerView.setAdapter(mAdapter);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanDashboard.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(ChairmanDashboard.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(ChairmanDashboard.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        chairmanDashBoardArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=chairmanDashBoardArray && chairmanDashBoardArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanDashBoardArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanDashboard.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_URL+"?school_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1,e);

                            totalStudents.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_student"));
                            noOfBoys.setText("Boys "+chairmanDashBoardArray.getJSONObject(0).getString("total_boys"));
                            noOfGirls.setText("Girls "+chairmanDashBoardArray.getJSONObject(0).getString("total_girls"));
                            totalAttendanceTaken.setText(chairmanDashBoardArray.getJSONObject(0).getString("attendence_taken"));
                            totalPresent.setText("Present "+chairmanDashBoardArray.getJSONObject(0).getString("attendence_present"));
                            totalAbsent.setText("Absent "+chairmanDashBoardArray.getJSONObject(0).getString("attendence_absent"));
                            totalFeeAmount.setText("Net Fee Amount: "+Rs+chairmanDashBoardArray.getJSONObject(0).getString("net_fee_amount")+"/-");
                            totalDiscount.setText("Discount: "+Rs+chairmanDashBoardArray.getJSONObject(0).getString("discount_amount")+"/-");
                            totalFeesCollections.setText("Total Fees: "+Rs+chairmanDashBoardArray.getJSONObject(0).getString("total_fee")+"/-");
                            totalPendingFee.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("fee_unpaid")+"/-");
                            totalFeePaid.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("fee_paid")+"/-");
                            totalFeeDue.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("fee_due_till_date")+"/-");
                            totalFeeNotYetDue.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("other_charges")+"/-");
                            dailyCollections.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("daily_collection")+"/-");
                            weeklyCollections.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("weekly_collection")+"/-");
                            monthlyCollections.setText(Rs+chairmanDashBoardArray.getJSONObject(0).getString("monthly_collection")+"/-");
                            totalAssignmentsUploaded.setText("Total "+chairmanDashBoardArray.getJSONObject(0).getString("total_asn"));
                            todayAssignment.setText("Today "+chairmanDashBoardArray.getJSONObject(0).getString("today_asn"));
                            weeklyAssignment.setText("Weekly "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_asn"));
                            totalNewAdmissions.setText("Session: "+chairmanDashBoardArray.getJSONObject(0).getString("crr_session")+"    -    "+chairmanDashBoardArray.getJSONObject(0).getString("crr_admissions_new"));
                            totalNewBoysAdmissions.setText("Boys "+chairmanDashBoardArray.getJSONObject(0).getString("crr_admissions_boys"));
                            totalNewGirlsAdmissions.setText("Girls "+chairmanDashBoardArray.getJSONObject(0).getString("crr_admissions_girls"));
                            totalNewAdmissions1.setText("Session: "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_session")+"    -    "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_admissions_new"));
                            totalNewBoysAdmissions1.setText("Boys "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_admissions_boys"));
                            totalNewGirlsAdmissions1.setText("Girls "+chairmanDashBoardArray.getJSONObject(0).getString("upcoming_admissions_girls"));


                            totalTeachers.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_employees"));
                            femaleteachers.setText("Female: "+chairmanDashBoardArray.getJSONObject(0).getString("total_female_employees"));
                            maleteachers.setText("Male: "+chairmanDashBoardArray.getJSONObject(0).getString("total_male_employees"));
                            totalVisitors.setText("Total: "+chairmanDashBoardArray.getJSONObject(0).getString("total_visitors"));
                            dailyVisitors.setText("Today: "+chairmanDashBoardArray.getJSONObject(0).getString("today_visitors"));
                            weeklyVisitors.setText("Weekly: "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_visitors"));
                            textTotalAccessPointsAnalysis.setText("Total: "+chairmanDashBoardArray.getJSONObject(0).getString("total_access_points"));
                            totalEmployeesAttendanceTaken.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_employee_attendance"));
                            totalEmployeesPresent.setText("Present "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_present_attendance"));
                            totalEmployeesAbsent.setText("Absent "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_absent_attendance"));
                            totalEmployeeLeaves.setText("Leave "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_leave_attendance"));
                            totalDiaryUploads.setText("Total: "+chairmanDashBoardArray.getJSONObject(0).getString("total_diary"));
                            totalDailyDiaryUploads.setText("Today: "+chairmanDashBoardArray.getJSONObject(0).getString("today_diary"));
                            totalWeeklyDiaryUploads.setText("Weekly: "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_diary"));

                            if(chairmanDashBoardArray.getJSONObject(0).getString("student_type_data").matches("") || chairmanDashBoardArray.getJSONObject(0).getString("student_type_data").matches("null"))
                            {

                            }
                            else {

                                categoryWise = new JSONArray(chairmanDashBoardArray.getJSONObject(0).getString("student_type_data"));


                                mAdapter = new ChairmanDashboardHorizontalListviewAdapter(ChairmanDashboard.this, categoryWise);
                                mRecyclerView.setAdapter(mAdapter);
                            }


                        }
                    }
                    else
                       // Utils.showToast(ChairmanDashboard.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(ChairmanDashboard.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
               // Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(ChairmanDashboard.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("school_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
               // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("session",Preferences.getInstance().session1);
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
        if(Utils.isNetworkAvailable(ChairmanDashboard.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(ChairmanDashboard.this, "Unable to fetch data, kindly enable internet settings!");
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
                        Intent intent = new Intent(ChairmanDashboard.this,AboutSchool.class);
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

                                Intent intent = new Intent(ChairmanDashboard.this, WebViewActivity.class);
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
