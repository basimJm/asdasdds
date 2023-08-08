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
import com.schoofi.adapters.ChairmanEmployeeAnalysisAdapter;
import com.schoofi.adapters.EmployeeListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChairmanEmployeeAnalysisDetails extends AppCompatActivity {

    private ImageView back;
    private JSONArray employeeListArray;
    private ChairmanEmployeeAnalysisAdapter employeeListAdapter;
    private ListView employeeDirectoryListView;
    String currentDate,currentDate1,dept_id,emp_id;
    String value ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chairman_employee_analysis_details);
        employeeDirectoryListView = (ListView) findViewById(R.id.listview_teacher_directory);
        value = getIntent().getStringExtra("value");
        currentDate = getIntent().getStringExtra("from");
        currentDate1 = getIntent().getStringExtra("to");
        dept_id = getIntent().getStringExtra("dept_id");
        emp_id = getIntent().getStringExtra("emp_id");
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d("kkkk",value+currentDate+dept_id);

        if(value.matches("0"))
        {

        }
        else {

            initData();
            getTeacherClassList();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(value.matches("0"))
        {

        }
        else {

            initData();
            getTeacherClassList();
        }

    }

    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+currentDate1+"&from_dt="+currentDate+"&dept_id="+dept_id+"&value="+value+"&emp_id="+emp_id);
            if(e == null)
            {
                employeeListArray= null;
            }
            else
            {
                employeeListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(employeeListArray!= null)
        {
            employeeListAdapter= new ChairmanEmployeeAnalysisAdapter(ChairmanEmployeeAnalysisDetails.this,employeeListArray);
            employeeDirectoryListView.setAdapter(employeeListAdapter);
            employeeListAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH_DETAILS;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Employee_Attendance"))

                    {

                        employeeListArray= new JSONObject(response).getJSONArray("Employee_Attendance");
                        if(null!=employeeListArray && employeeListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+currentDate1+"&from_dt="+currentDate+"&dept_id="+dept_id+"&value="+value+"&emp_id="+emp_id,e);
                            employeeDirectoryListView.invalidateViews();
                            employeeListAdapter= new ChairmanEmployeeAnalysisAdapter(ChairmanEmployeeAnalysisDetails.this,employeeListArray);
                            employeeDirectoryListView.setAdapter(employeeListAdapter);
                            employeeListAdapter.notifyDataSetChanged();
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
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
                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("from_dt",currentDate);
                params.put("to_dt",currentDate1);
                params.put("value",value);
                params.put("dept_id",dept_id);
                params.put("emp_id",emp_id);
                //params.put("crr_date",currentDate);
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

}
