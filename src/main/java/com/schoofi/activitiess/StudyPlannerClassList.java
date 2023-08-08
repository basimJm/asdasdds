package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.TeacherStudentAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudyPlannerClassList extends AppCompatActivity {

    private JSONArray teacherStudentAttendanceArray;
    TeacherStudentAttendanceAdapter teacherStudentAttendanceAdapter;
    private TextView screenTitle,teacherClass,teacherSection,newView1;
    private ImageView back,notify;
    private ListView teacherStudentAttendanceListView;
    String userEmailId,userId,token,teachId,value,userRoleId,date;
    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_study_planner_class_list);

        screenTitle = (TextView) findViewById(R.id.txt_teacherAttendance);
        teacherClass = (TextView) findViewById(R.id.text_class);
        teacherSection = (TextView) findViewById(R.id.text_section);
        teacherStudentAttendanceListView = (ListView) findViewById(R.id.teacher_class_list_listView);
        newView1 = (TextView) findViewById(R.id.newView1);
        newView1.setVisibility(View.INVISIBLE);
        back = (ImageView) findViewById(R.id.img_back);
        Preferences.getInstance().loadPreference(StudyPlannerClassList.this);

        userEmailId = Preferences.getInstance().userEmailId;
        token = Preferences.getInstance().token;
        userId = Preferences.getInstance().userId;
        userRoleId = Preferences.getInstance().userRoleId;

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

                Preferences.getInstance().loadPreference(StudyPlannerClassList.this);
                try {
                    Preferences.getInstance().studentSectionId = teacherStudentAttendanceArray.getJSONObject(position).getString("class_section_id");
                    Preferences.getInstance().studentClassId = teacherStudentAttendanceArray.getJSONObject(position).getString("class_id");
                    Preferences.getInstance().savePreference(StudyPlannerClassList.this);
                    if(Preferences.getInstance().schoolScheduleLevel.matches("Teacher")) {

                            Intent intent = new Intent(StudyPlannerClassList.this, TeacherSubjectListCollegePlannerNew.class);
                            intent.putExtra("position", position);
                            intent.putExtra("userId",userId);
                            intent.putExtra("userRoleId",userRoleId);
                            startActivity(intent);


                    }
                    else
                    {
                        Intent intent = new Intent(StudyPlannerClassList.this, TeacherSubjectListCollegeAttendance.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            }
        });


    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            Preferences.getInstance().loadPreference(StudyPlannerClassList.this);
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_CLASS_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+userRoleId+"&teac_id="+Preferences.getInstance().employeeId);
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
            teacherStudentAttendanceAdapter= new TeacherStudentAttendanceAdapter(StudyPlannerClassList.this,teacherStudentAttendanceArray);
            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
            teacherStudentAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudyPlannerClassList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_CLASS_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+userRoleId+"&teac_id="+Preferences.getInstance().employeeId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("utl",url);
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
                        Utils.showToast(StudyPlannerClassList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("class_List"))

                    {
                        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);
                        newView1.setVisibility(View.INVISIBLE);
                        teacherStudentAttendanceArray= new JSONObject(response).getJSONArray("class_List");
                        if(null!=teacherStudentAttendanceArray && teacherStudentAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray.toString().getBytes();
                            Preferences.getInstance().loadPreference(StudyPlannerClassList.this);
                            VolleySingleton.getInstance(StudyPlannerClassList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_CLASS_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+userRoleId+"&teac_id="+Preferences.getInstance().employeeId,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            teacherStudentAttendanceAdapter = new TeacherStudentAttendanceAdapter(StudyPlannerClassList.this, teacherStudentAttendanceArray);
                            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
                            teacherStudentAttendanceAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(StudyPlannerClassList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudyPlannerClassList.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(StudyPlannerClassList.this);
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
                    params.put("teac_id",Preferences.getInstance().employeeId);

                }

                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudyPlannerClassList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudyPlannerClassList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
