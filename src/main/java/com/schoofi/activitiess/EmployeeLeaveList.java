package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.StudentLeaveListAdapter;
import com.schoofi.adapters.TeacherSelfLeaveListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EmployeeLeaveList extends AppCompatActivity {

    private TextView leaveList;
    private ImageView backButton;
    private Button newLeave;
    private JSONArray studentLeaveListArray;
    private ListView studentLeaveList;
    private TeacherSelfLeaveListAdapter studentLeaveListAdapter;
    String fontPath = "fonts/asap.regular.ttf";
    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_leave_list);
        Typeface face = Typeface.createFromAsset(getAssets(), fontPath);
        leaveList = (TextView) findViewById(R.id.txt_LeaveList);
        backButton = (ImageView) findViewById(R.id.img_back);
        studentLeaveList = (ListView) findViewById(R.id.student_leave_list);
        //newLeave = (Button) findViewById(R.id.btn_student_new_leave_application);
        //newLeave.setTypeface(face);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initData();
        getStudentLeaveList();

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        Preferences.getInstance().loadPreference(getApplicationContext());

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentLeaveList();
            }
        });
        studentLeaveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View convertView, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                try {
                    Intent intent=new Intent(EmployeeLeaveList.this,EmployeeLeaveListDetails.class);
                    intent.putExtra("position", position);
                    intent.putExtra("leave_id",studentLeaveListArray.getJSONObject(position).getString("id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getStudentLeaveList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_leave, menu);
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
    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_APPROVAL_LEAVE_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&emp_id="+Preferences.getInstance().employeeId+"&user_id="+Preferences.getInstance().userId);
            if(e == null)
            {
                studentLeaveListArray= null;
            }
            else
            {
                studentLeaveListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentLeaveListArray!= null)
        {
            studentLeaveListAdapter= new TeacherSelfLeaveListAdapter(EmployeeLeaveList.this,studentLeaveListArray,"3");
            studentLeaveList.setAdapter(studentLeaveListAdapter);
            studentLeaveListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_APPROVAL_LEAVE_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                Log.d("kk",response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Leaves Found");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");

                    }
                    else
                    if(responseObject.has("Tech_LeaveList"))
                    {
                        studentLeaveList.setVisibility(View.VISIBLE);
                        studentLeaveListArray= new JSONObject(response).getJSONArray("Tech_LeaveList");
                        if(null!=studentLeaveListArray && studentLeaveListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentLeaveListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_APPROVAL_LEAVE_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&emp_id="+Preferences.getInstance().employeeId+"&user_id="+Preferences.getInstance().userId,e);
                            studentLeaveList.invalidateViews();
                            studentLeaveListAdapter = new TeacherSelfLeaveListAdapter(EmployeeLeaveList.this, studentLeaveListArray,"3");
                            studentLeaveList.setAdapter(studentLeaveListAdapter);
                            studentLeaveListAdapter.notifyDataSetChanged();
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
                Map<String,String> params = new HashMap<String, String>();

                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("emp_id",Preferences.getInstance().employeeId);
                params.put("employee_type",Preferences.getInstance().employeeType);
                params.put("user_id",Preferences.getInstance().userId);
                //params.put("crr_date",currentDate);
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
