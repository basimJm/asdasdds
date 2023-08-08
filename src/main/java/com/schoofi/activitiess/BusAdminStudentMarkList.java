package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.schoofi.adapters.BusStudentMarkScreenAdapter;
import com.schoofi.adapters.SchoolPlannerClassSectionListAdapter;
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


public class BusAdminStudentMarkList extends AppCompatActivity {

    private JSONArray teacherStudentAttendanceArray1,teacherStudentSubmittedAttendancearray;
    private ListView teacherStudentAttendanceListViewDetails;
    BusStudentMarkScreenAdapter busStudentMarkScreenAdapter;
    String busRoute,bus_no,date;
    private Button done;
    private ImageView back;
    private String indicator;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Bus Admin Student Mark List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_bus_admin_student_mark_list);

        teacherStudentAttendanceListViewDetails = (ListView) findViewById(R.id.teacher_homescreen_listview);

        busRoute = getIntent().getStringExtra("bus_route");
        //shift = getIntent().getStringExtra("shift");
        bus_no = getIntent().getStringExtra("bus_no");
        date = getIntent().getStringExtra("date");

        done = (Button) findViewById(R.id.btn_done);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                //push from top to bottom
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //push from top to bottom

                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        if(checkBox.isChecked())
        {
            indicator = "true";
        }

        else
        {
            indicator = "false";
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBox.isChecked()) {
                    indicator = "true";
                    for (int j = 0; j < teacherStudentSubmittedAttendancearray.length(); j++) {
                        try {
                            teacherStudentSubmittedAttendancearray.getJSONObject(j).put("date", date);
                            teacherStudentSubmittedAttendancearray.getJSONObject(j).put("isAdded", "A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                        Cache.Entry e = new Cache.Entry();
                        e.data = teacherStudentSubmittedAttendancearray.toString().getBytes();
                        VolleySingleton.getInstance(BusAdminStudentMarkList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&bus_no=" + bus_no + "&route_number=" + busRoute + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&day=" + date, e);
                        teacherStudentAttendanceListViewDetails.invalidateViews();
                        busStudentMarkScreenAdapter = new BusStudentMarkScreenAdapter(BusAdminStudentMarkList.this, teacherStudentSubmittedAttendancearray, bus_no, busRoute, date);
                        teacherStudentAttendanceListViewDetails.setAdapter(busStudentMarkScreenAdapter);
                        busStudentMarkScreenAdapter.notifyDataSetChanged();
                    }


                else {
                    indicator = "false";
                    for (int j = 0; j < teacherStudentSubmittedAttendancearray.length(); j++) {
                        try {
                            teacherStudentSubmittedAttendancearray.getJSONObject(j).put("date", date);
                            teacherStudentSubmittedAttendancearray.getJSONObject(j).put("isAdded", "N");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                        Cache.Entry e = new Cache.Entry();
                        e.data = teacherStudentSubmittedAttendancearray.toString().getBytes();
                        VolleySingleton.getInstance(BusAdminStudentMarkList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&bus_no=" + bus_no + "&route_number=" + busRoute + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&day=" + date, e);
                        teacherStudentAttendanceListViewDetails.invalidateViews();
                        busStudentMarkScreenAdapter = new BusStudentMarkScreenAdapter(BusAdminStudentMarkList.this, teacherStudentSubmittedAttendancearray, bus_no, busRoute, date);
                        teacherStudentAttendanceListViewDetails.setAdapter(busStudentMarkScreenAdapter);
                    }


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        //push from top to bottom
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&bus_no="+bus_no+"&route_number="+busRoute+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&day="+date);
            if(e == null)
            {
                teacherStudentSubmittedAttendancearray= null;
            }
            else
            {
                teacherStudentSubmittedAttendancearray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentSubmittedAttendancearray!= null)
        {
            for (int j = 0; j < teacherStudentSubmittedAttendancearray.length(); j++) {
                try {
                    teacherStudentSubmittedAttendancearray.getJSONObject(j).put("date", date);
                    teacherStudentSubmittedAttendancearray.getJSONObject(j).put("isAdded","N");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
            }
            busStudentMarkScreenAdapter = new BusStudentMarkScreenAdapter(BusAdminStudentMarkList.this,teacherStudentSubmittedAttendancearray,bus_no,busRoute,date);
            teacherStudentAttendanceListViewDetails.setAdapter(busStudentMarkScreenAdapter);
            busStudentMarkScreenAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(BusAdminStudentMarkList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                    if(responseObject.has("responseObject"))
                    {
                        teacherStudentSubmittedAttendancearray= new JSONObject(response).getJSONArray("responseObject");
                        for (int j = 0; j < teacherStudentSubmittedAttendancearray.length(); j++) {
                            teacherStudentSubmittedAttendancearray.getJSONObject(j).put("date", date);
                            teacherStudentSubmittedAttendancearray.getJSONObject(j).put("isAdded","N");
                            //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
                        }

                        System.out.print(teacherStudentSubmittedAttendancearray.toString());
                        if(null!=teacherStudentSubmittedAttendancearray && teacherStudentSubmittedAttendancearray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentSubmittedAttendancearray.toString().getBytes();
                            VolleySingleton.getInstance(BusAdminStudentMarkList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&bus_no="+bus_no+"&route_number="+busRoute+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&day="+date,e);
                            teacherStudentAttendanceListViewDetails.invalidateViews();
                            busStudentMarkScreenAdapter = new BusStudentMarkScreenAdapter(BusAdminStudentMarkList.this,teacherStudentSubmittedAttendancearray,bus_no,busRoute,date);
                            teacherStudentAttendanceListViewDetails.setAdapter(busStudentMarkScreenAdapter);
                            busStudentMarkScreenAdapter.notifyDataSetChanged();

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
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("date",date);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("bus_no",bus_no);
                params.put("route_number",busRoute);
                params.put("ins_id",Preferences.getInstance().institutionId);
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
