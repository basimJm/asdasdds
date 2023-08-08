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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import com.schoofi.adapters.AdminClassSectionListAdapter;
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

public class NewAdmissionSectionWise extends AppCompatActivity {

    JSONArray adminClassSectionListArray;
    ImageView back;
    SwipyRefreshLayout swipyRefreshLayout;
    ListView adminClassSectionListView;
    AdminClassSectionListAdapter adminClassSectionListAdapter;
    String classId,className,value,session;
    private TextView totalClassStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admin Class Section List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admin_class_section_list);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



       // Utils.showToast(getApplicationContext(),"po");

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);


        adminClassSectionListView = (ListView) findViewById(R.id.listViewAdminBusListView);
        Preferences.getInstance().loadPreference(getApplicationContext());



        classId = getIntent().getStringExtra("class_id");
        className = getIntent().getStringExtra("className");
        value = getIntent().getStringExtra("value3");
        session = getIntent().getStringExtra("session");
        totalClassStudents = (TextView) findViewById(R.id.text_class_students);

        //Log.d("opq",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_ADDMISSION_ANALYSIS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+session+"&filter="+"section_wise"+"&class_id="+classId);



        initData();
        getChairmanStudentLeaveList();

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getChairmanStudentLeaveList();
            }
        });

        adminClassSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                try {
                    Intent intent = new Intent(NewAdmissionSectionWise.this, NewAdmissionStudentWiseList.class);
                    intent.putExtra("sec_id", adminClassSectionListArray.getJSONObject(i).getString("section_id"));
                    intent.putExtra("class_id", classId);
                    intent.putExtra("session",session);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });



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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_ADDMISSION_ANALYSIS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+session+"&filter="+"section_wise"+"&class_id="+classId);
            if(e == null)
            {
                adminClassSectionListArray= null;
            }
            else
            {
                adminClassSectionListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminClassSectionListArray!= null)
        {

            adminClassSectionListAdapter = new AdminClassSectionListAdapter(NewAdmissionSectionWise.this, adminClassSectionListArray,className);
            adminClassSectionListView.setAdapter(adminClassSectionListAdapter);
            adminClassSectionListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(NewAdmissionSectionWise.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_ADDMISSION_ANALYSIS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("opq",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_ADDMISSION_ANALYSIS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+session+"&filter="+"section_wise"+"&class_id="+classId);
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
                    if(responseObject.has("list"))
                    {
                        adminClassSectionListArray= new JSONObject(response).getJSONArray("list");


                        if(null!=adminClassSectionListArray && adminClassSectionListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminClassSectionListArray.toString().getBytes();
                            VolleySingleton.getInstance(NewAdmissionSectionWise.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_ADDMISSION_ANALYSIS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+session+"&filter="+"section_wise"+"&class_id="+classId,e);
                            int count=0;
                            for(int i=0;i<adminClassSectionListArray.length();i++)
                            {
                                count= count+Integer.parseInt(adminClassSectionListArray.getJSONObject(i).getString("total_stu"))  ;
                            }

                            totalClassStudents.setText("Total Students :"+String.valueOf(count));

                            adminClassSectionListView.invalidateViews();

                            adminClassSectionListAdapter = new AdminClassSectionListAdapter(NewAdmissionSectionWise.this, adminClassSectionListArray,className);
                            adminClassSectionListView.setAdapter(adminClassSectionListAdapter);
                            adminClassSectionListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
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
                Preferences.getInstance().loadPreference(NewAdmissionSectionWise.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("session",session);
                params.put("filter","section_wise");
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("class_id", classId);
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
