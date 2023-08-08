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
import com.schoofi.adapters.HealthAndAuditUserTaskListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HealthAndAuditUserTaskList extends AppCompatActivity {

    private ListView healthAndAuditUsertaskListView;
    ImageView back;
    JSONArray healthAndAuditUserTaskListArray;
    HealthAndAuditUserTaskListAdapter healthAndAuditUserTaskListAdapter;
    String task_id,unique_id;
    String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Health And Audit User TaskList");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_health_and_audit_user_task_list);

        healthAndAuditUsertaskListView = (ListView) findViewById(R.id.listViewAddTask);
        back = (ImageView) findViewById(R.id.img_back);
        value = getIntent().getStringExtra("value");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(value.matches("1"))
        {
            initData();
            getClassList();
        }

        else
        {
            initData1();
            getClassList1();
        }



        healthAndAuditUsertaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    task_id = healthAndAuditUserTaskListArray.getJSONObject(position).getString("task_id");
                    unique_id = healthAndAuditUserTaskListArray.getJSONObject(position).getString("uniqueId");
                    Preferences.getInstance().loadPreference(getApplicationContext());

                    Intent intent = new Intent(HealthAndAuditUserTaskList.this,HealthAndAuditUserSubtaskList.class);
                    intent.putExtra("task_id",task_id);
                    intent.putExtra("unique_id",unique_id);
                    intent.putExtra("userId",Preferences.getInstance().userId);
                    if(value.matches("1"))
                    {
                        intent.putExtra("value","1");
                    }
                    else
                    {
                        intent.putExtra("value","2");
                    }
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });




    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if(value.matches("1"))
        {
            initData();
            getClassList();
        }

        else
        {
            initData1();
            getClassList1();
        }
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_TASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1");
            if(e == null)
            {
                healthAndAuditUserTaskListArray= null;
            }
            else
            {
                healthAndAuditUserTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditUserTaskListArray!= null)
        {
            healthAndAuditUserTaskListAdapter= new HealthAndAuditUserTaskListAdapter(HealthAndAuditUserTaskList.this,healthAndAuditUserTaskListArray);
            healthAndAuditUsertaskListView.setAdapter(healthAndAuditUserTaskListAdapter);
            healthAndAuditUserTaskListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditUserTaskList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_TASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1";
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
                        Utils.showToast(HealthAndAuditUserTaskList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditUserTaskList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("audit"))
                    {
                        healthAndAuditUserTaskListArray= new JSONObject(response).getJSONArray("audit");
                        if(null!=healthAndAuditUserTaskListArray && healthAndAuditUserTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditUserTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditUserTaskList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_TASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1",e);
                            healthAndAuditUsertaskListView.invalidateViews();
                            healthAndAuditUserTaskListAdapter= new HealthAndAuditUserTaskListAdapter(HealthAndAuditUserTaskList.this,healthAndAuditUserTaskListArray);
                            healthAndAuditUsertaskListView.setAdapter(healthAndAuditUserTaskListAdapter);
                            healthAndAuditUserTaskListAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(HealthAndAuditUserTaskList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(HealthAndAuditUserTaskList.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(HealthAndAuditUserTaskList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(HealthAndAuditUserTaskList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }

    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_TASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2");
            if(e == null)
            {
                healthAndAuditUserTaskListArray= null;
            }
            else
            {
                healthAndAuditUserTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditUserTaskListArray!= null)
        {
            healthAndAuditUserTaskListAdapter= new HealthAndAuditUserTaskListAdapter(HealthAndAuditUserTaskList.this,healthAndAuditUserTaskListArray);
            healthAndAuditUsertaskListView.setAdapter(healthAndAuditUserTaskListAdapter);
            healthAndAuditUserTaskListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditUserTaskList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_TASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2";
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
                        Utils.showToast(HealthAndAuditUserTaskList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditUserTaskList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("audit"))
                    {
                        healthAndAuditUserTaskListArray= new JSONObject(response).getJSONArray("audit");
                        if(null!=healthAndAuditUserTaskListArray && healthAndAuditUserTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditUserTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditUserTaskList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_TASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2",e);
                            healthAndAuditUsertaskListView.invalidateViews();
                            healthAndAuditUserTaskListAdapter= new HealthAndAuditUserTaskListAdapter(HealthAndAuditUserTaskList.this,healthAndAuditUserTaskListArray);
                            healthAndAuditUsertaskListView.setAdapter(healthAndAuditUserTaskListAdapter);
                            healthAndAuditUserTaskListAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(HealthAndAuditUserTaskList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(HealthAndAuditUserTaskList.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(HealthAndAuditUserTaskList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(HealthAndAuditUserTaskList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
