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

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.StudentSubjectCollegeAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SchoolPlannerSubjectList extends AppCompatActivity {

    private JSONArray teacherStudentAttendanceArray;
    StudentSubjectCollegeAdapter teacherStudentAttendanceAdapter;
    private ImageView back,notify;
    private ListView teacherStudentAttendanceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_school_planner_subject_list);

        teacherStudentAttendanceListView = (ListView) findViewById(R.id.teacher_homescreen_listview);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        getTeacherClassList();


        teacherStudentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                Preferences.getInstance().loadPreference(SchoolPlannerSubjectList.this);
                try {


                    Preferences.getInstance().savePreference(SchoolPlannerSubjectList.this);
                    Intent intent = new Intent(SchoolPlannerSubjectList.this,StudyPlannerAdminMainScreen.class);
                    intent.putExtra("position", position);
                    intent.putExtra("subject_id",teacherStudentAttendanceArray.getJSONObject(position).getString("subject_id"));
                    Preferences.getInstance().studentSubjectId = teacherStudentAttendanceArray.getJSONObject(position).getString("subject_id");
                    Preferences.getInstance().savePreference(getApplicationContext());


                    startActivity(intent);



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
            Preferences.getInstance().loadPreference(SchoolPlannerSubjectList.this);
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PLANNER_SUBJECT_LIST+"?cls_id="+Preferences.getInstance().studentClassId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token);
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
            teacherStudentAttendanceAdapter= new StudentSubjectCollegeAdapter(SchoolPlannerSubjectList.this,teacherStudentAttendanceArray,"2");
            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
            teacherStudentAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(SchoolPlannerSubjectList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PLANNER_SUBJECT_LIST+"?cls_id="+Preferences.getInstance().studentClassId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token;
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

                        Utils.showToast(SchoolPlannerSubjectList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("subject_List"))

                    {
                        teacherStudentAttendanceListView.setVisibility(View.VISIBLE);

                        teacherStudentAttendanceArray= new JSONObject(response).getJSONArray("subject_List");
                        if(null!=teacherStudentAttendanceArray && teacherStudentAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray.toString().getBytes();
                            Preferences.getInstance().loadPreference(SchoolPlannerSubjectList.this);
                            VolleySingleton.getInstance(SchoolPlannerSubjectList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PLANNER_SUBJECT_LIST+"?cls_id="+Preferences.getInstance().studentClassId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            teacherStudentAttendanceAdapter = new StudentSubjectCollegeAdapter(SchoolPlannerSubjectList.this, teacherStudentAttendanceArray,"2");
                            teacherStudentAttendanceListView.setAdapter(teacherStudentAttendanceAdapter);
                            teacherStudentAttendanceAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(SchoolPlannerSubjectList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(SchoolPlannerSubjectList.this, "Error fetching modules! Please try after sometime.");
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

                Preferences.getInstance().loadPreference(SchoolPlannerSubjectList.this);
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
        if(Utils.isNetworkAvailable(SchoolPlannerSubjectList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(SchoolPlannerSubjectList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
