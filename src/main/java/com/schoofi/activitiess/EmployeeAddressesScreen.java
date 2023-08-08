package com.schoofi.activitiess;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.EmployeeAddressesAdapter;
import com.schoofi.adapters.HealthAndAuditListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import polypicker.model.Image;

public class EmployeeAddressesScreen extends AppCompatActivity {

    private ImageView back;
    private ListView employeeAddressListView;
    private EmployeeAddressesAdapter employeeAddressesAdapter;
    private JSONArray employeeAddressArray;
    private String emp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_addresses_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        employeeAddressListView = (ListView) findViewById(R.id.student_leave_list);
        emp_id = getIntent().getStringExtra("emp_id");
        if(emp_id.matches("") || emp_id.matches("null"))
        {

        }
        else
        {
            Log.d("pp1",emp_id);
            initData();
            getClassList();

        }







    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_ADDRESSES+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&emp_id="+emp_id+"&sch_id="+Preferences.getInstance().schoolId);
            if(e == null)
            {
                employeeAddressArray= null;
            }
            else
            {
                employeeAddressArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(employeeAddressArray!= null)
        {
            employeeAddressesAdapter= new EmployeeAddressesAdapter(EmployeeAddressesScreen.this,employeeAddressArray);
            employeeAddressListView.setAdapter(employeeAddressesAdapter);
            employeeAddressesAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(EmployeeAddressesScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_ADDRESSES+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&emp_id="+emp_id+"&sch_id="+Preferences.getInstance().schoolId;
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
                        Utils.showToast(EmployeeAddressesScreen.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(EmployeeAddressesScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("list"))
                    {
                        employeeAddressArray= new JSONObject(response).getJSONArray("list");
                        if(null!= employeeAddressArray&& employeeAddressArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeAddressArray.toString().getBytes();
                            VolleySingleton.getInstance(EmployeeAddressesScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_ADDRESSES+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&emp_id="+emp_id+"&sch_id="+Preferences.getInstance().schoolId,e);
                            employeeAddressListView.invalidateViews();
                            employeeAddressesAdapter= new EmployeeAddressesAdapter(EmployeeAddressesScreen.this,employeeAddressArray);
                            employeeAddressListView.setAdapter(employeeAddressesAdapter);
                            employeeAddressesAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(EmployeeAddressesScreen.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(EmployeeAddressesScreen.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(EmployeeAddressesScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(EmployeeAddressesScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
}
