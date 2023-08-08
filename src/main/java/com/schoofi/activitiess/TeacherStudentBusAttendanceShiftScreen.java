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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.BusRouteListAdapter;
import com.schoofi.adapters.HealthAndAuditAdapter;
import com.schoofi.adapters.TeacherStudentBusAttendanceBusListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherHomeScreenVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherStudentBusAttendanceShiftScreen extends AppCompatActivity {

    public ArrayList<TeacherHomeScreenVO> temparr;
    //TeacherHomeScreenAdapter teacherHomeScreenAdapter;
    private ListView healthAndAuditListView;
    private JSONArray teacherStudentBusListArray;
    BusRouteListAdapter busRouteListAdapter;
    ImageView back;
    String bus_route,bus_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Teacher Student Bus Attendance Shift Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_health_and_audit_home_screen);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*temparr=new ArrayList<TeacherHomeScreenVO>();
        TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Morning", "1");
        TeacherHomeScreenVO tecHomeScreenVO1 = new TeacherHomeScreenVO("Evening", "2");


        temparr.add(tecHomeScreenVO);
        temparr.add(tecHomeScreenVO1);*/

       // bus_route = getIntent().getStringExtra("bus_route");
        bus_number =getIntent().getStringExtra("bus_no");


        healthAndAuditListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        /*HealthAndAuditAdapter healthAndAuditAdapter = new HealthAndAuditAdapter(getApplicationContext(),temparr);
        healthAndAuditListView.setAdapter(healthAndAuditAdapter);*/

        healthAndAuditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TeacherStudentBusAttendanceShiftScreen.this,TeacherStudentBusAttendance.class);
                try {
                    intent.putExtra("bus_route",teacherStudentBusListArray.getJSONObject(i).getString("route_no"));
                    intent.putExtra("bus_no",bus_number);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //intent.putExtra("shift",temparr.get(i).getFieldName());

            }
        });

        initData();
        getChairmanStudentLeaveList();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getChairmanStudentLeaveList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_ROUTES+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&bus_number="+bus_number);
            if(e == null)
            {
                teacherStudentBusListArray= null;
            }
            else
            {
                teacherStudentBusListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentBusListArray!= null)
        {

            busRouteListAdapter = new BusRouteListAdapter(TeacherStudentBusAttendanceShiftScreen.this, teacherStudentBusListArray);
            healthAndAuditListView.setAdapter(busRouteListAdapter);
            busRouteListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherStudentBusAttendanceShiftScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_ROUTES;//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("VehicleList"))
                    {
                        teacherStudentBusListArray= new JSONObject(response).getJSONArray("VehicleList");


                        if(null!=teacherStudentBusListArray && teacherStudentBusListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentBusListArray.toString().getBytes();
                            VolleySingleton.getInstance(TeacherStudentBusAttendanceShiftScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_ROUTES+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&bus_number="+bus_number,e);
                            healthAndAuditListView.invalidateViews();

                            busRouteListAdapter = new BusRouteListAdapter(TeacherStudentBusAttendanceShiftScreen.this, teacherStudentBusListArray);
                            healthAndAuditListView.setAdapter(busRouteListAdapter);
                            busRouteListAdapter.notifyDataSetChanged();

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
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("bus_number",bus_number);
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
