package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.EmployeeLeaveLevelUpdateAdapter;
import com.schoofi.adapters.HealthAndAuditListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeLeaveLevelUpdates extends AppCompatActivity {

    private ImageView back;
    private ListView employeeLevelUpdate;
    private SwipyRefreshLayout swipyRefreshLayout;
    private JSONArray employeeLeaveLevelUpdateArray;
    private EmployeeLeaveLevelUpdateAdapter employeeLeaveLevelUpdateAdapter;
    private String leave_id;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_leave_level);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leave_id = getIntent().getStringExtra("leave_id");

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        employeeLevelUpdate = (ListView) findViewById(R.id.listview_student_submitted_polls);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                
           initData();
           getClassList();

            }
        });

        initData();
        getClassList();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getClassList();
    }

    private void initData()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.APPROVAL_MATRIX_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&emp_id="+Preferences.getInstance().employeeId+"&leave_id="+leave_id+"&user_id="+Preferences.getInstance().userId);
            if(e == null)
            {
                employeeLeaveLevelUpdateArray= null;
            }
            else
            {
                employeeLeaveLevelUpdateArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(employeeLeaveLevelUpdateArray!= null)
        {
            employeeLeaveLevelUpdateAdapter= new EmployeeLeaveLevelUpdateAdapter(EmployeeLeaveLevelUpdates.this,employeeLeaveLevelUpdateArray);
            employeeLevelUpdate.setAdapter(employeeLeaveLevelUpdateAdapter);
            employeeLeaveLevelUpdateAdapter.notifyDataSetChanged();
            
        }
    }

    protected void getClassList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(EmployeeLeaveLevelUpdates.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.APPROVAL_MATRIX_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&emp_id="+Preferences.getInstance().employeeId+"&leave_id="+leave_id+"&user_id="+Preferences.getInstance().userId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(EmployeeLeaveLevelUpdates.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(EmployeeLeaveLevelUpdates.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Tech_LeaveList"))
                    {
                        employeeLeaveLevelUpdateArray= new JSONObject(response).getJSONArray("Tech_LeaveList");
                        if(null!=employeeLeaveLevelUpdateArray && employeeLeaveLevelUpdateArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeLeaveLevelUpdateArray.toString().getBytes();
                            VolleySingleton.getInstance(EmployeeLeaveLevelUpdates.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.APPROVAL_MATRIX_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&emp_id="+Preferences.getInstance().employeeId+"&leave_id="+leave_id+"&user_id="+Preferences.getInstance().userId,e);
                            employeeLevelUpdate.invalidateViews();
                            employeeLeaveLevelUpdateAdapter= new EmployeeLeaveLevelUpdateAdapter(EmployeeLeaveLevelUpdates.this,employeeLeaveLevelUpdateArray);
                            employeeLevelUpdate.setAdapter(employeeLeaveLevelUpdateAdapter);
                            employeeLeaveLevelUpdateAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(EmployeeLeaveLevelUpdates.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(EmployeeLeaveLevelUpdates.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(EmployeeLeaveLevelUpdates.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(EmployeeLeaveLevelUpdates.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.health_and_audit_audits_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
