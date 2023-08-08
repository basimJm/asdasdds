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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.TeacherCoordinaterPendingFeesStudentWiseAdapter;
import com.schoofi.adapters.TeacherCoordinaterPendingLeavesAnalysisAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TeacherCoordinaterPendingFeesStudentWise extends AppCompatActivity {

    private JSONArray teacherStudentBusListArray;
    private TeacherCoordinaterPendingFeesStudentWiseAdapter teacherStudentBusAttendanceBusListAdapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private ListView teacherStudentAttendanceListView;
    private TextView teacherPendingLeavesTotalCount;
    ImageView back;
    String secId,classId,className,sectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_coordinater_pending_fees_student_wise);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        teacherStudentAttendanceListView = (ListView) findViewById(R.id.listViewAdminBusListView);
        teacherPendingLeavesTotalCount = (TextView) findViewById(R.id.txt_teacherLeave);
        secId = getIntent().getStringExtra("sec_id");
        classId = getIntent().getStringExtra("class_id");
        className = getIntent().getStringExtra("class_names");
        sectionName = getIntent().getStringExtra("section_name");

        teacherStudentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*try {
                    Intent intent = new Intent(TeacherCoordinaterBusAttendanceAnalysisMainScreen.this,TeacherCoordinatorBusAttendanceAnalysisRouteAnalysis.class);
                    //intent.putExtra("bus_route",teacherStudentBusListArray.getJSONObject(i).getString("route_no"));
                    intent.putExtra("bus_number",teacherStudentBusListArray.getJSONObject(i).getString("bus_number"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                Intent intent = new Intent(TeacherCoordinaterPendingFeesStudentWise.this,StudentProfile.class);
                try {
                    Preferences.getInstance().loadPreference(getApplicationContext());
                    Preferences.getInstance().studentId= teacherStudentBusListArray.getJSONObject(i).getString("stu_id");
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

       // initData();
        getChairmanStudentLeaveList();

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                //initData();
                getChairmanStudentLeaveList();
            }
        });



    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATER_PENDING_FEES_STUDENT_WISE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&sec_id="+secId+"&cls_id="+classId);
            if(e == null)
            {
                teacherStudentBusListArray= null;
            }
            else
            {
                teacherStudentBusListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentBusListArray!= null)
        {

            teacherStudentBusAttendanceBusListAdapter = new TeacherCoordinaterPendingFeesStudentWiseAdapter(TeacherCoordinaterPendingFeesStudentWise.this, teacherStudentBusListArray);
            teacherStudentAttendanceListView.setAdapter(teacherStudentBusAttendanceBusListAdapter);
            teacherStudentBusAttendanceBusListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATER_PENDING_FEES_STUDENT_WISE;//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
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
                        // JSONObject jsonObject = new JSONObject(responseObject.getString("sectionwise_pending_leaves"));
                        //JSONObject jsonObject = new JSONObject(String.valueOf(responseObject));

                       // teacherPendingLeavesTotalCount.setText("Total Pending Leaves: "+responseObject.getJSONObject("responseObject").getString("pending_leaves"));

                        teacherStudentBusListArray= new JSONObject(response).getJSONArray("responseObject");


                        if(null!=teacherStudentBusListArray && teacherStudentBusListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentBusListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATER_PENDING_FEES_STUDENT_WISE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&sec_id="+secId+"&cls_id="+classId,e);
                            teacherStudentAttendanceListView.invalidateViews();

                            teacherPendingLeavesTotalCount.setText("Class: "+className+"-"+sectionName);

                            teacherStudentBusAttendanceBusListAdapter = new TeacherCoordinaterPendingFeesStudentWiseAdapter(TeacherCoordinaterPendingFeesStudentWise.this, teacherStudentBusListArray);
                            teacherStudentAttendanceListView.setAdapter(teacherStudentBusAttendanceBusListAdapter);
                            teacherStudentBusAttendanceBusListAdapter.notifyDataSetChanged();
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
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("session",Preferences.getInstance().session1);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("cls_id",classId);
                params.put("sec_id",secId);

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
