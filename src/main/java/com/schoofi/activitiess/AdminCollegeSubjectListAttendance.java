package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.HashMap;
import java.util.Map;

public class AdminCollegeSubjectListAttendance extends AppCompatActivity {


    private JSONArray teacherStudentAttendanceArray;
    StudentSubjectCollegeAdapter teacherStudentAttendanceAdapter;
    private TextView screenTitle,teacherClass,teacherSection,newView1;
    private ImageView back,notify;
    private ListView teacherStudentAttendanceListView;
    String userEmailId,userId,token,teachId;
    private SwipyRefreshLayout swipyRefreshLayout;


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
        teacherStudentAttendanceListView = (ListView) findViewById(R.id.teacher_class_list_listView);
        newView1 = (TextView) findViewById(R.id.newView1);
        newView1.setVisibility(View.INVISIBLE);
        back = (ImageView) findViewById(R.id.img_back);
        notify = (ImageView) findViewById(R.id.img_notify);


        Preferences.getInstance().loadPreference(AdminCollegeSubjectListAttendance.this);

        if(Preferences.getInstance().userRoleId.matches("3"))
        {
            notify.setVisibility(View.VISIBLE);
        }
        else
        {
            notify.setVisibility(View.GONE);
        }

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCollegeSubjectListAttendance.this,AttendanceNotifyScreen.class);
                startActivity(intent);
            }
        });

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
        initData();
        getTeacherClassList();

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getTeacherClassList();
            }
        });

        teacherStudentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                Preferences.getInstance().loadPreference(AdminCollegeSubjectListAttendance.this);
                try {
                    Preferences.getInstance().studentSectionId = teacherStudentAttendanceArray.getJSONObject(position).getString("class_section_id");
                    Preferences.getInstance().studentClassId = teacherStudentAttendanceArray.getJSONObject(position).getString("class_id");
                    Preferences.getInstance().savePreference(AdminCollegeSubjectListAttendance.this);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Intent intent = new Intent(AdminCollegeSubjectListAttendance.this,TeacherStudentAttendanceDetails.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
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
            Preferences.getInstance().loadPreference(AdminCollegeSubjectListAttendance.this);
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_ATTENDANCE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().teachId);
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
            teacherStudentAttendanceAdapter= new StudentSubjectCollegeAdapter(AdminCollegeSubjectListAttendance.this,teacherStudentAttendanceArray,"2");
            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
            teacherStudentAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminCollegeSubjectListAttendance.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_ATTENDANCE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().teachId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);
                        newView1.setVisibility(View.VISIBLE);
                        //Utils.showToast(TeacherStudentAttendance.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        teacherStudentAttendanceListView.setVisibility(View.INVISIBLE);
                        newView1.setVisibility(View.INVISIBLE);
                        Utils.showToast(AdminCollegeSubjectListAttendance.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Tech_SecList"))

                    {
                        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);
                        newView1.setVisibility(View.INVISIBLE);
                        teacherStudentAttendanceArray= new JSONObject(response).getJSONArray("Tech_SecList");
                        if(null!=teacherStudentAttendanceArray && teacherStudentAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray.toString().getBytes();
                            Preferences.getInstance().loadPreference(AdminCollegeSubjectListAttendance.this);
                            VolleySingleton.getInstance(AdminCollegeSubjectListAttendance.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_ATTENDANCE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId+"&teac_id="+Preferences.getInstance().teachId,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            teacherStudentAttendanceAdapter = new StudentSubjectCollegeAdapter(AdminCollegeSubjectListAttendance.this, teacherStudentAttendanceArray,"2");
                            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
                            teacherStudentAttendanceAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(AdminCollegeSubjectListAttendance.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminCollegeSubjectListAttendance.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(AdminCollegeSubjectListAttendance.this);
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
                //System.out.print("lll");
                if(Preferences.getInstance().userRoleId.matches("3"))
                {
                    Log.d("kkkk","kkk");
                }

                else {
                    System.out.print("lllllmmmm");
                    params.put("teac_id",Preferences.getInstance().teachId);

                }

                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AdminCollegeSubjectListAttendance.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminCollegeSubjectListAttendance.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
