package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.schoofi.adapters.ChairmanEmployeeDepartwiseCountAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChairmanEmployeeDepartmentWiseCount extends AppCompatActivity {

    private ListView employeeDirectoryListView;
    private ImageView calendar,back,calendar1;
    private JSONArray chairmanEmployeeDepartmentWiseArray;
    private ChairmanEmployeeDepartwiseCountAdapter chairmanEmployeeDepartwiseCountAdapter;
    String value;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String from1,to1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chairman_employee_department_wise_count);

        back = (ImageView) findViewById(R.id.img_back);

        employeeDirectoryListView = (ListView) findViewById(R.id.listview_teacher_directory);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }


        });

        value = getIntent().getStringExtra("value");
        if(value.matches("0"))
        {
            from1 = currentDate;
            to1 = currentDate;
            initData();
            getTeacherClassList();
        }

        else
        {
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");
            initData();
            getTeacherClassList();
        }

        calendar = (ImageView) findViewById(R.id.img_calender);

        calendar1 = (ImageView) findViewById(R.id.img_calender1);
        calendar1.setVisibility(View.GONE);
        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(ChairmanEmployeeDepartmentWiseCount.this,ChairmanEmployeeAttendanceEmployeeWise.class);
                    intent.putExtra("startingDate",from1);
                    intent.putExtra("endingDate",to1);
                    intent.putExtra("value","2");
                    intent.putExtra("dept_id",chairmanEmployeeDepartmentWiseArray.getJSONObject(0).getString("dept_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(ChairmanEmployeeDepartmentWiseCount.this,EmployeeAttendanceScreen.class);
                    intent.putExtra("value",3);
                    intent.putExtra("dept_id","");

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);




            }


        });

        employeeDirectoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(value.matches("0"))
                {

                    try {
                        Intent intent = new Intent(ChairmanEmployeeDepartmentWiseCount.this,ChairmanEmployeeWithoutGraphAnalysisScreen.class);
                        intent.putExtra("startingDate",currentDate);
                        intent.putExtra("endingDate",currentDate);
                        intent.putExtra("value","2");
                        intent.putExtra("dept_id",chairmanEmployeeDepartmentWiseArray.getJSONObject(position).getString("dept_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {

                    try {
                        Intent intent = new Intent(ChairmanEmployeeDepartmentWiseCount.this,ChairmanEmployeeWithoutGraphAnalysisScreen.class);
                        intent.putExtra("startingDate",from1);
                        intent.putExtra("endingDate",to1);
                        intent.putExtra("value","2");
                        intent.putExtra("dept_id",chairmanEmployeeDepartmentWiseArray.getJSONObject(position).getString("dept_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ALL_DEPARTMENT_ATTENDANCE_COUNT+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+from1+"&from_dt="+to1);
            if(e == null)
            {
                chairmanEmployeeDepartmentWiseArray= null;
            }
            else
            {
                chairmanEmployeeDepartmentWiseArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanEmployeeDepartmentWiseArray!= null)
        {
            employeeDirectoryListView.setVisibility(View.VISIBLE);
            chairmanEmployeeDepartwiseCountAdapter= new ChairmanEmployeeDepartwiseCountAdapter(ChairmanEmployeeDepartmentWiseCount.this,chairmanEmployeeDepartmentWiseArray);
            employeeDirectoryListView.setAdapter(chairmanEmployeeDepartwiseCountAdapter);
            chairmanEmployeeDepartwiseCountAdapter.notifyDataSetChanged();
        }
        else
        {
            employeeDirectoryListView.setVisibility(View.GONE);
        }
    }

    protected void getTeacherClassList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ALL_DEPARTMENT_ATTENDANCE_COUNT;
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
                        employeeDirectoryListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        employeeDirectoryListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("Employee_Attendance"))

                    {

                        chairmanEmployeeDepartmentWiseArray= new JSONObject(response).getJSONArray("Employee_Attendance");
                        if(null!=chairmanEmployeeDepartmentWiseArray && chairmanEmployeeDepartmentWiseArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanEmployeeDepartmentWiseArray.toString().getBytes();
                            employeeDirectoryListView.setVisibility(View.VISIBLE);
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ALL_DEPARTMENT_ATTENDANCE_COUNT+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+from1+"&from_dt="+to1,e);
                            employeeDirectoryListView.invalidateViews();
                            chairmanEmployeeDepartwiseCountAdapter= new ChairmanEmployeeDepartwiseCountAdapter(ChairmanEmployeeDepartmentWiseCount.this,chairmanEmployeeDepartmentWiseArray);
                            employeeDirectoryListView.setAdapter(chairmanEmployeeDepartwiseCountAdapter);
                            chairmanEmployeeDepartwiseCountAdapter.notifyDataSetChanged();

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
                params.put("from_dt",from1);
                params.put("to_dt",to1);

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
