package com.schoofi.activitiess;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.schoofi.adapters.EmployeeAttendanceListViewAdapter;
import com.schoofi.adapters.StudentCustomAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
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

public class EmployeeAttendanceMainScreen extends AppCompatActivity {

    private ListView employeeAttendanceListView;
    private EmployeeAttendanceListViewAdapter employeeAttendanceListViewAdapter;
    private JSONArray employeeAttendanceArray;

    private ImageView back,calendarIcon;


    int year_x,month_x,day_x;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    static final int dialog_id=1;
    String from1,to1;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    private TextView newView;

    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    Date date = new Date();


    private Context context;

    SwipyRefreshLayout swipyRefreshLayout;
    int value;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_attendance_main_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarIcon = (ImageView) findViewById(R.id.img_calendar_icon);
        value = getIntent().getIntExtra("value",1);

        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeAttendanceMainScreen.this,EmployeeAttendanceScreen.class);
                intent.putExtra("value",value);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }


        });


        if(value== 1)
        {
            cal.add(Calendar.DATE, -7);
            Date todate1 = cal.getTime();
            from1 = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
            //System.out.println(fromDate);
            cal1.add(Calendar.DATE, -1);
            Date todate2 = cal1.getTime();
            to1 = new SimpleDateFormat("yyyy-MM-dd").format(todate2);
            newView = (TextView) findViewById(R.id.newView1);
            employeeAttendanceListView = (ListView) findViewById(R.id.listView_student_daily_attendance);
            swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
            swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {

                    initData();
                    getStudentCustomAttendance();

                }
            });

            initData();
            getStudentCustomAttendance();
        }
        else
            if(value ==3)
            {
                employeeAttendanceListView = (ListView) findViewById(R.id.listView_student_daily_attendance);
                from1 = getIntent().getStringExtra("startingDate");
                to1 = getIntent().getStringExtra("endingDate");

                System.out.print(from1);
                System.out.print(to1);

                newView = (TextView) findViewById(R.id.newView1);
                swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
                swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(SwipyRefreshLayoutDirection direction) {

                        initData();
                        getStudentCustomAttendance();

                    }
                });

                initData();
                getStudentCustomAttendance();
            }
        else
        {
            employeeAttendanceListView = (ListView) findViewById(R.id.listView_student_daily_attendance);
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");

           // System.out.print(from1);
           // System.out.print(to1);

            newView = (TextView) findViewById(R.id.newView1);
            swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
            swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {

                    initData();
                    getStudentCustomAttendance();

                }
            });

            if(from1 == null || to1 == null)
            {
                Log.d("ju","po");
            }
            else
            {initData();
                getStudentCustomAttendance();}


        }








    }



    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_ATTENDANCE+"?emp_id="+ Preferences.getInstance().employeeId+"&token="+Preferences.getInstance().token+"&fromdt="+from1+"&todt="+to1+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                employeeAttendanceArray = null;
            }
            else
            {
                employeeAttendanceArray = new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(employeeAttendanceArray!= null)
        {
            employeeAttendanceListViewAdapter= new EmployeeAttendanceListViewAdapter(EmployeeAttendanceMainScreen.this,employeeAttendanceArray);
            employeeAttendanceListView.setAdapter(employeeAttendanceListViewAdapter);
            employeeAttendanceListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentCustomAttendance()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("opp",response);

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        newView.setVisibility(View.VISIBLE);
                        employeeAttendanceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                       // Utils.showToast(context, "Session Expired:Please Login Again");
                        newView.setVisibility(View.VISIBLE);
                        employeeAttendanceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("Employee_Attendance"))
                    {
                        newView.setVisibility(View.INVISIBLE);
                        employeeAttendanceListView.setVisibility(View.VISIBLE);
                        employeeAttendanceArray= new JSONObject(response).getJSONArray("Employee_Attendance");
                        if(null!=employeeAttendanceArray && employeeAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_ATTENDANCE+"?emp_id="+ Preferences.getInstance().employeeId+"&token="+Preferences.getInstance().token+"&fromdt="+from1+"&todt="+to1+"&device_id="+Preferences.getInstance().deviceId,e);
                            employeeAttendanceListView.invalidateViews();
                            employeeAttendanceListViewAdapter = new EmployeeAttendanceListViewAdapter(EmployeeAttendanceMainScreen.this, employeeAttendanceArray);
                            employeeAttendanceListView.setAdapter(employeeAttendanceListViewAdapter);
                            employeeAttendanceListViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        //Utils.showToast(context, "Error Fetching Response");
                    System.out.println("op");
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                   // Utils.showToast(context, "Error fetching modules! Please try after sometime.");
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
               // Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("emp_id",Preferences.getInstance().employeeId);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                //System.out.print("dd"+from1);
                Log.d("dd",from1+to1);
                params.put("fromdt",from1);
                params.put("todt",to1);
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
