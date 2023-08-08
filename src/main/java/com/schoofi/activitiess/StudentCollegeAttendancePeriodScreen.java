package com.schoofi.activitiess;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.StudentCollegePeriodListAdapter;
import com.schoofi.adapters.StudentSubjectCollegeAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class StudentCollegeAttendancePeriodScreen extends AppCompatActivity {

    private JSONArray teacherStudentAttendanceArray;
    StudentCollegePeriodListAdapter teacherStudentAttendanceAdapter;
    private TextView screenTitle,teacherClass,teacherSection,newView1;
    private ImageView back,notify;
    private ListView teacherStudentAttendanceListView;
    String userEmailId,userId,token,teachId;
    private SwipyRefreshLayout swipyRefreshLayout;
    HorizontalCalendar horizontalCalendar;
    String dat,subject_id,value,userRoleId;
    final Context context = this;
    String selectedDate1,selectedDate2;
    Date selectedDate3;
    private Calendar calendar;
    private int year, month, day;
    private TextView dateView;
    String date1,date2;
    String year1,month1,day1;
    private Button submit;
    Date date3,date4;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String attendance_date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_college_attendance_period_screen);

        Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Teacher StudentAttendance");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        screenTitle = (TextView) findViewById(R.id.txt_teacherAttendance);
        teacherClass = (TextView) findViewById(R.id.text_class);
        teacherSection = (TextView) findViewById(R.id.text_section);
        teacherStudentAttendanceListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        subject_id = getIntent().getStringExtra("subject_id");

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        value = getIntent().getStringExtra("value");
        //notify = (ImageView) findViewById(R.id.img_notify);


        Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);

        if(value.matches("2"))
        {
            userId = getIntent().getStringExtra("userId");
            userRoleId = getIntent().getStringExtra("userRoleId");
            attendance_date = getIntent().getStringExtra("attendance_date");
        }
        else
        {
            userId = Preferences.getInstance().userId;
            userRoleId = Preferences.getInstance().userRoleId;

        }



        userEmailId = Preferences.getInstance().userEmailId;
        token = Preferences.getInstance().token;

        teachId = Preferences.getInstance().teachId;

        Log.d("uio","kkk"+teachId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(value.matches("2"))
        {
            initData1();
            getTeacherClassList1();
        }
        else {

            initData();
            getTeacherClassList();
        }

        teacherStudentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
                try {

                    Preferences.getInstance().savePreference(StudentCollegeAttendancePeriodScreen.this);
                    if(value.matches("2"))
                    {
                        Intent intent = new Intent(StudentCollegeAttendancePeriodScreen.this,TeacherStudentCollegeAttendanceNew.class);
                        intent.putExtra("position", position);
                        intent.putExtra("subject_id",subject_id);
                        intent.putExtra("periodId",teacherStudentAttendanceArray.getJSONObject(position).getString("period_no"));
                        intent.putExtra("value","2");
                        intent.putExtra("attendance_date",attendance_date);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(StudentCollegeAttendancePeriodScreen.this,TeacherStudentCollegeAttendanceNew.class);
                        intent.putExtra("position", position);
                        intent.putExtra("subject_id",subject_id);
                        intent.putExtra("periodId",teacherStudentAttendanceArray.getJSONObject(position).getString("period_no"));
                        intent.putExtra("value","1");
                        startActivity(intent);
                    }



                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.COLLEGE_STUDENT_PERIOD_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId);
            if(e == null)
            {
                teacherStudentAttendanceArray= null;
            }
            else
            {
                teacherStudentAttendanceArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentAttendanceArray!= null)
        {
            teacherStudentAttendanceAdapter= new StudentCollegePeriodListAdapter(StudentCollegeAttendancePeriodScreen.this,teacherStudentAttendanceArray);
            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
            teacherStudentAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentCollegeAttendancePeriodScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.COLLEGE_STUDENT_PERIOD_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);

                        Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("period_list"))

                    {
                        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);

                        teacherStudentAttendanceArray= new JSONObject(response).getJSONArray("period_list");
                        if(null!=teacherStudentAttendanceArray && teacherStudentAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray.toString().getBytes();
                            Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
                            VolleySingleton.getInstance(StudentCollegeAttendancePeriodScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.COLLEGE_STUDENT_PERIOD_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            teacherStudentAttendanceAdapter = new StudentCollegePeriodListAdapter(StudentCollegeAttendancePeriodScreen.this, teacherStudentAttendanceArray);
                            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
                            teacherStudentAttendanceAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Error fetching modules! Please try after sometime.");
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
                Map<String,String> params = new HashMap<String, String>();

                Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
                params.put("token",Preferences.getInstance().token);

                params.put("u_email_id",Preferences.getInstance().userEmailId);

                params.put("u_id",Preferences.getInstance().userId);

                params.put("device_id", Preferences.getInstance().deviceId);

                params.put("ins_id",Preferences.getInstance().institutionId);

                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("role_id",Preferences.getInstance().userRoleId);
                params.put("section_id",Preferences.getInstance().studentSectionId);


                if(Preferences.getInstance().userRoleId.matches("3"))
                {
                    Log.d("kkkk","kkk");
                }

                else {
                    System.out.print("lllllmmmm");
                    params.put("teac_id",Preferences.getInstance().employeeId);

                }


                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentCollegeAttendancePeriodScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void initData1()
    {


        try
        {
            Cache.Entry e;
            Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_PERIOD_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId+"&class_id="+Preferences.getInstance().studentClassId+"&date="+attendance_date+"&subject_id="+subject_id);
            if(e == null)
            {
                teacherStudentAttendanceArray= null;
            }
            else
            {
                teacherStudentAttendanceArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentAttendanceArray!= null)
        {
            teacherStudentAttendanceAdapter= new StudentCollegePeriodListAdapter(StudentCollegeAttendancePeriodScreen.this,teacherStudentAttendanceArray);
            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
            teacherStudentAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentCollegeAttendancePeriodScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_PERIOD_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId+"&class_id="+Preferences.getInstance().studentClassId+"&date="+attendance_date+"&subject_id="+subject_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);

                        Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("period_list"))

                    {
                        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);

                        teacherStudentAttendanceArray= new JSONObject(response).getJSONArray("period_list");
                        if(null!=teacherStudentAttendanceArray && teacherStudentAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray.toString().getBytes();
                            Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
                            VolleySingleton.getInstance(StudentCollegeAttendancePeriodScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_PERIOD_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId+"&class_id="+Preferences.getInstance().studentClassId+"&date="+attendance_date+"&subject_id="+subject_id,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            teacherStudentAttendanceAdapter = new StudentCollegePeriodListAdapter(StudentCollegeAttendancePeriodScreen.this, teacherStudentAttendanceArray);
                            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
                            teacherStudentAttendanceAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Error fetching modules! Please try after sometime.");
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
                Map<String,String> params = new HashMap<String, String>();

                Preferences.getInstance().loadPreference(StudentCollegeAttendancePeriodScreen.this);
                params.put("token",Preferences.getInstance().token);

                params.put("u_email_id",Preferences.getInstance().userEmailId);

                params.put("user_id",userId);

                params.put("device_id", Preferences.getInstance().deviceId);

                params.put("ins_id",Preferences.getInstance().institutionId);

                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("role_id",userRoleId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("subject_id",subject_id);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("date",attendance_date);


                if(Preferences.getInstance().userRoleId.matches("3"))
                {
                    Log.d("kkkk","kkk");
                }

                else {
                    System.out.print("lllllmmmm");
                    params.put("teac_id",Preferences.getInstance().employeeId);

                }


                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentCollegeAttendancePeriodScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentCollegeAttendancePeriodScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
