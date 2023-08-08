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
import com.schoofi.adapters.ChairmanStudentFragmentFeesClassAdapter;
import com.schoofi.adapters.StudentEventListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.fragments.ChairmanStudentFragmentFees;
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

public class ChairmanStudentFragmentFeesClass extends AppCompatActivity {

    ImageView back;
    ListView feeClass;
    ChairmanStudentFragmentFeesClassAdapter chairmanStudentFragmentFeesClassAdapter;
    private JSONArray ChairmanStudentFragmentFeesClassArray;
    int value;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Student Fragment Fees Class");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_student_fragment_fees_class);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        value = getIntent().getExtras().getInt("value");
        temp = getIntent().getStringExtra("temp");


        feeClass = (ListView) findViewById(R.id.listViewAddTask);

        //initData();
        getChairmanStudentLeaveList();

        feeClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Intent intent = new Intent(ChairmanStudentFragmentFeesClass.this,ChairmanStudentFragmentFeesSection.class);
                    intent.putExtra("value",value);
                    intent.putExtra("cls_id",ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("class_id"));
                    intent.putExtra("temp",temp);
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
        //initData();
        getChairmanStudentLeaveList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_CLASS+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&temp="+temp+"&value="+value);
            if(e == null)
            {
                ChairmanStudentFragmentFeesClassArray= null;
            }
            else
            {
                ChairmanStudentFragmentFeesClassArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(ChairmanStudentFragmentFeesClassArray!= null)
        {
            feeClass.invalidateViews();
            chairmanStudentFragmentFeesClassAdapter= new ChairmanStudentFragmentFeesClassAdapter(ChairmanStudentFragmentFeesClass.this,ChairmanStudentFragmentFeesClassArray,value,temp);
            feeClass.setAdapter(chairmanStudentFragmentFeesClassAdapter);
            chairmanStudentFragmentFeesClassAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentFragmentFeesClass.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_CLASS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               System.out.println(response);
               // System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(ChairmanStudentFragmentFeesClass.this, "No Records Found");
                        feeClass.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(ChairmanStudentFragmentFeesClass.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("fee"))
                    {
                        ChairmanStudentFragmentFeesClassArray= new JSONObject(response).getJSONArray("fee");
                        if(null!=ChairmanStudentFragmentFeesClassArray && ChairmanStudentFragmentFeesClassArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = ChairmanStudentFragmentFeesClassArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanStudentFragmentFeesClass.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_CLASS+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&temp="+temp+"&value="+value,e);
                            feeClass.setVisibility(View.VISIBLE);
                            feeClass.invalidateViews();
//                            float sum=0;
//                            for(int i=0;i<ChairmanStudentFragmentFeesClassArray.length();i++)
//                            {
//                                sum = sum+Float.parseFloat(ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("total_fee_amount"));
//                            }
//                            System.out.print(sum);
                            chairmanStudentFragmentFeesClassAdapter= new ChairmanStudentFragmentFeesClassAdapter(ChairmanStudentFragmentFeesClass.this,ChairmanStudentFragmentFeesClassArray,value,temp);
                            feeClass.setAdapter(chairmanStudentFragmentFeesClassAdapter);
                            chairmanStudentFragmentFeesClassAdapter.notifyDataSetChanged();
                            //swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(ChairmanStudentFragmentFeesClass.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(ChairmanStudentFragmentFeesClass.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(ChairmanStudentFragmentFeesClass.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("cls_id", Preferences.getInstance().studentClassId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("temp",temp);
                params.put("value",String.valueOf(value));
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(ChairmanStudentFragmentFeesClass.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(ChairmanStudentFragmentFeesClass.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
