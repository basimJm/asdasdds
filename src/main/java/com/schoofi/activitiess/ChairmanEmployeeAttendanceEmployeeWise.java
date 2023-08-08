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
import com.schoofi.adapters.ChairmanEmployeeAttendanceDetailsAdapter;
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

public class ChairmanEmployeeAttendanceEmployeeWise extends AppCompatActivity {

    private ListView employeeDirectoryListView;
    private ImageView calendar,back;
    private JSONArray chairmanEmployeeDepartmentWiseArray;
    private ChairmanEmployeeAttendanceDetailsAdapter chairmanEmployeeDepartwiseCountAdapter;
    String value;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String from1,to1,dept_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chairman_employee_attendance_employee_wise);

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
            dept_id = "null";
            initData();
            getTeacherClassList();
        }

        else
        {
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");
            dept_id = getIntent().getStringExtra("dept_id");
            initData();
            getTeacherClassList();
        }

//        calendar = (ImageView) findViewById(R.id.img_calender);
//
//        calendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ChairmanEmployeeDepartmentWiseCount.this,EmployeeAttendanceScreen.class);
//                intent.putExtra("value",3);
//                intent.putExtra("dept_id","");
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//
//
//
//
//            }
//
//
//        });

        employeeDirectoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(value.matches("0"))
                {

                    try {
                        Intent intent = new Intent(ChairmanEmployeeAttendanceEmployeeWise.this,ChairmanEmployeeAnalysisDetails.class);
                        intent.putExtra("from",from1);
                        intent.putExtra("to",to1);
                        intent.putExtra("value","4");
                        intent.putExtra("dept_id",dept_id);
                        intent.putExtra("emp_id",chairmanEmployeeDepartmentWiseArray.getJSONObject(position).getString("emp_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {

                    try {
                        Intent intent = new Intent(ChairmanEmployeeAttendanceEmployeeWise.this,ChairmanEmployeeAnalysisDetails.class);
                        intent.putExtra("from",from1);
                        intent.putExtra("to",to1);
                        intent.putExtra("value","4");
                        intent.putExtra("dept_id",dept_id);
                        intent.putExtra("emp_id",chairmanEmployeeDepartmentWiseArray.getJSONObject(position).getString("emp_id"));
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ALL_EMPLOYEE_DEPARTMENT_ATTENDANCE_COUNT+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+from1+"&from_dt="+to1+"&dept_id="+dept_id);
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
            chairmanEmployeeDepartwiseCountAdapter= new ChairmanEmployeeAttendanceDetailsAdapter(ChairmanEmployeeAttendanceEmployeeWise.this,chairmanEmployeeDepartmentWiseArray);
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
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ALL_EMPLOYEE_DEPARTMENT_ATTENDANCE_COUNT;
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
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ALL_EMPLOYEE_DEPARTMENT_ATTENDANCE_COUNT+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+from1+"&from_dt="+to1+"&dept_id="+dept_id,e);
                            employeeDirectoryListView.invalidateViews();
                            chairmanEmployeeDepartwiseCountAdapter= new ChairmanEmployeeAttendanceDetailsAdapter(ChairmanEmployeeAttendanceEmployeeWise.this,chairmanEmployeeDepartmentWiseArray);
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
                params.put("dept_id",dept_id);

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
