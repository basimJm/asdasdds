package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.AdminEmployeeLeaveListAdapter;
import com.schoofi.adapters.TeacherStudentLeaveListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminEmployeeLeaveList extends AppCompatActivity {

    String schoolId = Preferences.getInstance().schoolId;
    String classId = Preferences.getInstance().studentClassId;
    String sectionId = Preferences.getInstance().studentSectionId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    private ListView teacherStudentListView;
    AdminEmployeeLeaveListAdapter teacherStudentLeaveListAdapter;
    private TextView screenTitle;
    private ImageView back;
    private JSONArray teacherStudentLeaveListArray;
    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_employee_leave_list);

        screenTitle = (TextView) findViewById(R.id.txt_teacherLeaveList);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        teacherStudentListView = (ListView) findViewById(R.id.teacher_student_leave_list);
        initData();
        getTeacherStudentLeaveList();

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getTeacherStudentLeaveList();
            }
        });

        teacherStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                try {
                    Preferences.getInstance().studentId = teacherStudentLeaveListArray.getJSONObject(position).getString("student_id");

                    Preferences.getInstance().savePreference(AdminEmployeeLeaveList.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Intent intent = new Intent(AdminEmployeeLeaveList.this,TeacherLeaveDetails.class);
                    intent.putExtra("position", position);
                    intent.putExtra("name",teacherStudentLeaveListArray.getJSONObject(position).getString("stu_name"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });



    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_student_leave, menu);
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getTeacherStudentLeaveList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_LIST_URL+"?sch_id="+schoolId+"&token="+token+"&u_email_id="+userEmailId+"&u_id="+userId+"&cls_id="+classId+"&sec_id="+sectionId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                teacherStudentLeaveListArray= null;
            }
            else
            {
                teacherStudentLeaveListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentLeaveListArray!= null)
        {
            teacherStudentLeaveListAdapter= new AdminEmployeeLeaveListAdapter(AdminEmployeeLeaveList.this,teacherStudentLeaveListArray);
            teacherStudentListView.setAdapter(teacherStudentLeaveListAdapter);
            teacherStudentLeaveListAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_LIST_URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(getApplicationContext(),"No Leaves Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Tech_LeaveList"))
                    {
                        teacherStudentLeaveListArray= new JSONObject(response).getJSONArray("Tech_LeaveList");
                        if(null!=teacherStudentLeaveListArray && teacherStudentLeaveListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentLeaveListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_LIST_URL+"?sch_id="+schoolId+"&token="+token+"&u_email_id="+userEmailId+"&u_id="+userId+"&cls_id="+classId+"&sec_id="+sectionId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            teacherStudentListView.invalidateViews();
                            teacherStudentLeaveListAdapter = new AdminEmployeeLeaveListAdapter(AdminEmployeeLeaveList.this, teacherStudentLeaveListArray);
                            teacherStudentListView.setAdapter(teacherStudentLeaveListAdapter);
                            teacherStudentLeaveListAdapter.notifyDataSetChanged();
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
                params.put("sch_id",schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",token);
                params.put("u_email_id",userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",userId);
                params.put("cls_id", classId);
                params.put("sec_id", sectionId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
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
