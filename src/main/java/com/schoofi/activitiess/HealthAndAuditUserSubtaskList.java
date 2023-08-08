package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.schoofi.adapters.HealthAndAuditUserSubTaskListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HealthAndAuditUserSubtaskList extends AppCompatActivity {

    ListView healthAndAuditUserSubTaskListView;
    JSONArray healthAndAuditUserSubTaskListArray;
    HealthAndAuditUserSubTaskListAdapter healthAndAuditUserSubTaskListAdapter;
    ImageView back;
    String taskId,unique_id,userId;
    String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Health And Audit User SubtaskList");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_health_and_audit_user_subtask_list);
        back = (ImageView) findViewById(R.id.img_back);
        healthAndAuditUserSubTaskListView = (ListView) findViewById(R.id.listViewAddTask);
        taskId = getIntent().getStringExtra("task_id");
        unique_id = getIntent().getStringExtra("unique_id");
        value = getIntent().getStringExtra("value");
        //Utils.showToast(getApplicationContext(),value);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(value.matches("3"))
        {
            userId = getIntent().getStringExtra("userId");
            initData2();
            getClassList2();
        }

        else {


            if (value.matches("1")) {
                initData();
                getClassList();
            } else {
                initData1();
                getClassList1();
            }
        }



        healthAndAuditUserSubTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(value.matches("1")) {
                    Intent intent = new Intent(HealthAndAuditUserSubtaskList.this, HealthAndAuditUserSubTaskListDetails.class);
                    intent.putExtra("position", position);
                    intent.putExtra("task_id", taskId);
                    intent.putExtra("unique_id", unique_id);
                    intent.putExtra("value","1");
                    startActivity(intent);
                }

                else
                if(value.matches("3")) {
                    Intent intent = new Intent(HealthAndAuditUserSubtaskList.this, HealthAndAuditChairmanSubtaskListDetails.class);
                    intent.putExtra("position", position);
                    intent.putExtra("task_id", taskId);
                    intent.putExtra("unique_id", unique_id);
                    intent.putExtra("userId",userId);
                    intent.putExtra("value","3");
                    startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent(HealthAndAuditUserSubtaskList.this, HealthAndAuditUserTaskSubmit.class);
                    intent.putExtra("position", position);
                    intent.putExtra("task_id", taskId);
                    intent.putExtra("unique_id", unique_id);
                    intent.putExtra("value","2");
                    try {
                        intent.putExtra("task_area",healthAndAuditUserSubTaskListArray.getJSONObject(position).getString("task_area"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (value.matches("3")) {
            userId = getIntent().getStringExtra("userId");
            initData2();
            getClassList2();
        } else {

            if (value.matches("1")) {
                initData();
                getClassList();
            } else {
                initData1();
                getClassList1();
           }
        }
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1"+"&task_id="+taskId+"&unique_id="+unique_id);
            if(e == null)
            {
                healthAndAuditUserSubTaskListArray= null;
            }
            else
            {
                healthAndAuditUserSubTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditUserSubTaskListArray!= null)
        {
            healthAndAuditUserSubTaskListAdapter= new HealthAndAuditUserSubTaskListAdapter(HealthAndAuditUserSubtaskList.this,healthAndAuditUserSubTaskListArray);
            healthAndAuditUserSubTaskListView.setAdapter(healthAndAuditUserSubTaskListAdapter);
            healthAndAuditUserSubTaskListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditUserSubtaskList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1"+"&task_id="+taskId+"&unique_id="+unique_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
                        Utils.showToast(HealthAndAuditUserSubtaskList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditUserSubtaskList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("audit"))
                    {
                        healthAndAuditUserSubTaskListArray= new JSONObject(response).getJSONArray("audit");
                        if(null!=healthAndAuditUserSubTaskListArray && healthAndAuditUserSubTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditUserSubTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditUserSubtaskList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1"+"&task_id="+taskId+"&unique_id="+unique_id,e);
                            healthAndAuditUserSubTaskListView.invalidateViews();
                            healthAndAuditUserSubTaskListAdapter= new HealthAndAuditUserSubTaskListAdapter(HealthAndAuditUserSubtaskList.this,healthAndAuditUserSubTaskListArray);
                            healthAndAuditUserSubTaskListView.setAdapter(healthAndAuditUserSubTaskListAdapter);
                            healthAndAuditUserSubTaskListAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(HealthAndAuditUserSubtaskList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(HealthAndAuditUserSubtaskList.this, "Error fetching modules! Please try after sometime.");
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
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(HealthAndAuditUserSubtaskList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(HealthAndAuditUserSubtaskList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }

    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+value+"&task_id="+taskId+"&unique_id="+unique_id);
            if(e == null)
            {
                healthAndAuditUserSubTaskListArray= null;
            }
            else
            {
                healthAndAuditUserSubTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditUserSubTaskListArray!= null)
        {
            healthAndAuditUserSubTaskListAdapter= new HealthAndAuditUserSubTaskListAdapter(HealthAndAuditUserSubtaskList.this,healthAndAuditUserSubTaskListArray);
            healthAndAuditUserSubTaskListView.setAdapter(healthAndAuditUserSubTaskListAdapter);
            healthAndAuditUserSubTaskListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditUserSubtaskList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+value+"&task_id="+taskId+"&unique_id="+unique_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
                        Utils.showToast(HealthAndAuditUserSubtaskList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditUserSubtaskList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("audit"))
                    {
                        healthAndAuditUserSubTaskListArray= new JSONObject(response).getJSONArray("audit");
                        if(null!=healthAndAuditUserSubTaskListArray && healthAndAuditUserSubTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditUserSubTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditUserSubtaskList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+value+"&task_id="+taskId+"&unique_id="+unique_id,e);
                            healthAndAuditUserSubTaskListView.invalidateViews();
                            healthAndAuditUserSubTaskListAdapter= new HealthAndAuditUserSubTaskListAdapter(HealthAndAuditUserSubtaskList.this,healthAndAuditUserSubTaskListArray);
                            healthAndAuditUserSubTaskListView.setAdapter(healthAndAuditUserSubTaskListAdapter);
                            healthAndAuditUserSubTaskListAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(HealthAndAuditUserSubtaskList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(HealthAndAuditUserSubtaskList.this, "Error fetching modules! Please try after sometime.");
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
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(HealthAndAuditUserSubtaskList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(HealthAndAuditUserSubtaskList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    private void initData2()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"3"+"&task_id="+taskId+"&unique_id="+unique_id);
            if(e == null)
            {
                healthAndAuditUserSubTaskListArray= null;
            }
            else
            {
                healthAndAuditUserSubTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditUserSubTaskListArray!= null)
        {
            healthAndAuditUserSubTaskListAdapter= new HealthAndAuditUserSubTaskListAdapter(HealthAndAuditUserSubtaskList.this,healthAndAuditUserSubTaskListArray);
            healthAndAuditUserSubTaskListView.setAdapter(healthAndAuditUserSubTaskListAdapter);
            healthAndAuditUserSubTaskListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList2()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditUserSubtaskList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"3"+"&task_id="+taskId+"&unique_id="+unique_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
                        Utils.showToast(HealthAndAuditUserSubtaskList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditUserSubtaskList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("audit"))
                    {
                        healthAndAuditUserSubTaskListArray= new JSONObject(response).getJSONArray("audit");
                        if(null!=healthAndAuditUserSubTaskListArray && healthAndAuditUserSubTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditUserSubTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditUserSubtaskList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"3"+"&task_id="+taskId+"&unique_id="+unique_id,e);
                            healthAndAuditUserSubTaskListView.invalidateViews();
                            healthAndAuditUserSubTaskListAdapter= new HealthAndAuditUserSubTaskListAdapter(HealthAndAuditUserSubtaskList.this,healthAndAuditUserSubTaskListArray);
                            healthAndAuditUserSubTaskListView.setAdapter(healthAndAuditUserSubTaskListAdapter);
                            healthAndAuditUserSubTaskListAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(HealthAndAuditUserSubtaskList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(HealthAndAuditUserSubtaskList.this, "Error fetching modules! Please try after sometime.");
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
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(HealthAndAuditUserSubtaskList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(HealthAndAuditUserSubtaskList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
}
