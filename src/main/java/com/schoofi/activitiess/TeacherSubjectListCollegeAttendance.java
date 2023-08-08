package com.schoofi.activitiess;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
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
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class TeacherSubjectListCollegeAttendance extends AppCompatActivity {


    private JSONArray teacherStudentAttendanceArray;
    StudentSubjectCollegeAdapter teacherStudentAttendanceAdapter;
    private TextView screenTitle,teacherClass,teacherSection,newView1;
    private ImageView back,notify;
    private ListView teacherStudentAttendanceListView;
    String userEmailId,userId,token,teachId;
    private SwipyRefreshLayout swipyRefreshLayout;
    HorizontalCalendar horizontalCalendar;
    String dat;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_subject_list_college_attendance);

        Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Teacher StudentAttendance");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        screenTitle = (TextView) findViewById(R.id.txt_teacherAttendance);
        teacherClass = (TextView) findViewById(R.id.text_class);
        teacherSection = (TextView) findViewById(R.id.text_section);
        teacherStudentAttendanceListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        //newView1 = (TextView) findViewById(R.id.newView1);
        //newView1.setVisibility(View.INVISIBLE);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //notify = (ImageView) findViewById(R.id.img_notify);


        Preferences.getInstance().loadPreference(TeacherSubjectListCollegeAttendance.this);

//        if(Preferences.getInstance().userRoleId.matches("3"))
//        {
//            notify.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            notify.setVisibility(View.GONE);
//        }
//
//        notify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TeacherSubjectListCollegeAttendance.this,AttendanceNotifyScreen.class);
//                startActivity(intent);
//            }
//        });

        userEmailId = Preferences.getInstance().userEmailId;
        token = Preferences.getInstance().token;
        userId = Preferences.getInstance().userId;
        teachId = Preferences.getInstance().teachId;

        Log.d("uio","kkk"+teachId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date0, int position) {

                dat = new SimpleDateFormat("yyyy-MM-dd").format(date0);
                selectedDate1 = (dat);
                selectedDate2 = new SimpleDateFormat("dd-MMM-yyyy").format(date0);
                date = dat;

                initData();
                getTeacherClassList();
            }

        });
        initData();
        getTeacherClassList();

//        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
//
//        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//                initData();
//                getTeacherClassList();
//            }
//        });

        teacherStudentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                Preferences.getInstance().loadPreference(TeacherSubjectListCollegeAttendance.this);
                try {

                    Preferences.getInstance().savePreference(TeacherSubjectListCollegeAttendance.this);
                    Intent intent = new Intent(TeacherSubjectListCollegeAttendance.this,TeacherStudentCollegeAttendance.class);
                    intent.putExtra("position", position);
                    intent.putExtra("subject_id",teacherStudentAttendanceArray.getJSONObject(position).getString("subject_id"));
                    intent.putExtra("periodId",teacherStudentAttendanceArray.getJSONObject(position).getString("period_number"));
                    intent.putExtra("crr_date",date);
                    startActivity(intent);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        initData();
        getTeacherClassList();
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_student_attendance, menu);
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

    private void initData()
    {


        try
        {
            Cache.Entry e;
            Preferences.getInstance().loadPreference(TeacherSubjectListCollegeAttendance.this);
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SUBJECT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId+"&date="+date);
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
            teacherStudentAttendanceAdapter= new StudentSubjectCollegeAdapter(TeacherSubjectListCollegeAttendance.this,teacherStudentAttendanceArray,"2");
            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
            teacherStudentAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherSubjectListCollegeAttendance.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SUBJECT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId+"&date="+date;
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
                       // newView1.setVisibility(View.VISIBLE);
                        //Utils.showToast(TeacherStudentAttendance.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);
                       // newView1.setVisibility(View.INVISIBLE);
                        Utils.showToast(TeacherSubjectListCollegeAttendance.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("subject_list"))

                    {
                        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);
//                        newView1.setVisibility(View.INVISIBLE);
                        teacherStudentAttendanceArray= new JSONObject(response).getJSONArray("subject_list");
                        if(null!=teacherStudentAttendanceArray && teacherStudentAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray.toString().getBytes();
                            Preferences.getInstance().loadPreference(TeacherSubjectListCollegeAttendance.this);
                            VolleySingleton.getInstance(TeacherSubjectListCollegeAttendance.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SUBJECT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().employeeId+"&section_id="+Preferences.getInstance().studentSectionId+"&date="+date,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            teacherStudentAttendanceAdapter = new StudentSubjectCollegeAdapter(TeacherSubjectListCollegeAttendance.this, teacherStudentAttendanceArray,"2");
                            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
                            teacherStudentAttendanceAdapter.notifyDataSetChanged();
                            //swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(TeacherSubjectListCollegeAttendance.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(TeacherSubjectListCollegeAttendance.this, "Error fetching modules! Please try after sometime.");
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
                //params.put("student_ID",Preferences.getInstance().studentId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                //System.out.print("lll");
                Preferences.getInstance().loadPreference(TeacherSubjectListCollegeAttendance.this);
                params.put("token",Preferences.getInstance().token);
                //System.out.print("lll");
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //System.out.print("lll");
                params.put("u_id",Preferences.getInstance().userId);
                //System.out.print("lll");
                params.put("device_id", Preferences.getInstance().deviceId);
                //System.out.print("lll");
                params.put("ins_id",Preferences.getInstance().institutionId);
                //System.out.print("lll");
                params.put("sch_id",Preferences.getInstance().schoolId);
                //System.out.print("lll");
                params.put("role_id",Preferences.getInstance().userRoleId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("date",date);
                //System.out.print("lll");
                if(Preferences.getInstance().userRoleId.matches("3"))
                {
                    Log.d("kkkk","kkk");
                }

                else {
                    System.out.print("lllllmmmm");
                    params.put("teac_id",Preferences.getInstance().employeeId);

                }

                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(TeacherSubjectListCollegeAttendance.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(TeacherSubjectListCollegeAttendance.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
