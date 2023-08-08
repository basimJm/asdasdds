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

public class ChairmanStudentResultSectionList extends AppCompatActivity {

    private JSONArray chairmanStudentResultSectionListArray;
    private ListView chairmanStudentResultSectionListView;
    AdminClassSectionListAdapter adminClassSectionListAdapter;
    ImageView back;
    String classId;
    String className;
    TextView className1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Student Result Section List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_student_result_section_list);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        classId = getIntent().getStringExtra("class_id");
        className = getIntent().getStringExtra("className");

        className1 = (TextView) findViewById(R.id.text_admin_class);
        chairmanStudentResultSectionListView = (ListView) findViewById(R.id.listViewAddTask);
        className1.setText("Class "+className);

        initData();
        getChairmanStudentLeaveList();

        className1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChairmanStudentResultSectionList.this,ChairmanStudentTermExamList.class);
                intent.putExtra("classId",classId);
                intent.putExtra("sectionId","");
                startActivity(intent);
            }
        });

        chairmanStudentResultSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Intent intent = new Intent(ChairmanStudentResultSectionList.this,ChairmanStudentTermExamList.class);
                    intent.putExtra("classId",classId);
                    intent.putExtra("sectionId",chairmanStudentResultSectionListArray.getJSONObject(i).getString("class_section_id"));
                    startActivity(intent);
                } catch (JSONException e) {
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId);
            if(e == null)
            {
                chairmanStudentResultSectionListArray= null;
            }
            else
            {
                chairmanStudentResultSectionListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanStudentResultSectionListArray!= null)
        {

            adminClassSectionListAdapter = new AdminClassSectionListAdapter(ChairmanStudentResultSectionList.this, chairmanStudentResultSectionListArray,className);
            chairmanStudentResultSectionListView.setAdapter(adminClassSectionListAdapter);
            adminClassSectionListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentResultSectionList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_SECTION_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(ChairmanStudentResultSectionList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(ChairmanStudentResultSectionList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("classSectionStudents"))
                    {
                        chairmanStudentResultSectionListArray= new JSONObject(response).getJSONArray("classSectionStudents");


                        if(null!=chairmanStudentResultSectionListView && chairmanStudentResultSectionListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanStudentResultSectionListArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanStudentResultSectionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId,e);
                            chairmanStudentResultSectionListView.invalidateViews();

                            adminClassSectionListAdapter = new AdminClassSectionListAdapter(ChairmanStudentResultSectionList.this, chairmanStudentResultSectionListArray,className);
                            chairmanStudentResultSectionListView.setAdapter(adminClassSectionListAdapter);
                            adminClassSectionListAdapter.notifyDataSetChanged();
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
                params.put("cls_id", classId);
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
