package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.schoofi.adapters.AdminClassListAdapter;
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


public class SchoolPlannerClassSelectionList extends AppCompatActivity {

    private JSONArray schoolPlannerClassSectionListArray;
    private ListView schoolPlannerClassSectionListView;
    private SchoolPlannerClassSectionListAdapter schoolPlannerClassSectionListAdapter;
    private String indicator;
    private CheckBox checkBox;
    private Button done;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("School Planner Class Section List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_school_planner_class_selection_list);

        schoolPlannerClassSectionListView = (ListView) findViewById(R.id.school_section_list);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        done = (Button) findViewById(R.id.btn_done);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //push from top to bottom
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //push from top to bottom
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

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

                if(checkBox.isChecked())
                {
                    indicator = "true";
                    for (int j = 0; j < schoolPlannerClassSectionListArray.length(); j++) {
                        try {
                            schoolPlannerClassSectionListArray.getJSONObject(j).put("isAdded", "A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)

                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = schoolPlannerClassSectionListArray.toString().getBytes();
                    VolleySingleton.getInstance(SchoolPlannerClassSelectionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
                    schoolPlannerClassSectionListView.invalidateViews();

                    schoolPlannerClassSectionListAdapter = new SchoolPlannerClassSectionListAdapter(SchoolPlannerClassSelectionList.this, schoolPlannerClassSectionListArray,indicator);
                    schoolPlannerClassSectionListView.setAdapter(schoolPlannerClassSectionListAdapter);
                    schoolPlannerClassSectionListAdapter.notifyDataSetChanged();
                }

                else {
                    indicator = "false";
                    for (int j = 0; j < schoolPlannerClassSectionListArray.length(); j++) {
                        try {
                            schoolPlannerClassSectionListArray.getJSONObject(j).put("isAdded", "N");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)

                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = schoolPlannerClassSectionListArray.toString().getBytes();
                    VolleySingleton.getInstance(SchoolPlannerClassSelectionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId, e);
                    schoolPlannerClassSectionListView.invalidateViews();

                    schoolPlannerClassSectionListAdapter = new SchoolPlannerClassSectionListAdapter(SchoolPlannerClassSelectionList.this, schoolPlannerClassSectionListArray, indicator);
                    schoolPlannerClassSectionListView.setAdapter(schoolPlannerClassSectionListAdapter);
                    schoolPlannerClassSectionListAdapter.notifyDataSetChanged();
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

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                schoolPlannerClassSectionListArray= null;
            }
            else
            {
                schoolPlannerClassSectionListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(schoolPlannerClassSectionListArray!= null)
        {

            schoolPlannerClassSectionListAdapter = new SchoolPlannerClassSectionListAdapter(SchoolPlannerClassSelectionList.this, schoolPlannerClassSectionListArray,indicator);
            schoolPlannerClassSectionListView.setAdapter(schoolPlannerClassSectionListAdapter);
            schoolPlannerClassSectionListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(SchoolPlannerClassSelectionList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
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
                        schoolPlannerClassSectionListArray= new JSONObject(response).getJSONArray("responseObject");

                        for (int j = 0; j < schoolPlannerClassSectionListArray.length(); j++) {
                            schoolPlannerClassSectionListArray.getJSONObject(j).put("isAdded", "A");
                            //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
                        }


                        if(null!=schoolPlannerClassSectionListArray && schoolPlannerClassSectionListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolPlannerClassSectionListArray.toString().getBytes();
                            VolleySingleton.getInstance(SchoolPlannerClassSelectionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
                            schoolPlannerClassSectionListView.invalidateViews();

                            schoolPlannerClassSectionListAdapter = new SchoolPlannerClassSectionListAdapter(SchoolPlannerClassSelectionList.this, schoolPlannerClassSectionListArray,indicator);
                            schoolPlannerClassSectionListView.setAdapter(schoolPlannerClassSectionListAdapter);
                            schoolPlannerClassSectionListAdapter.notifyDataSetChanged();
                            //swipyRefreshLayout.setRefreshing(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        //push from top to bottom
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
