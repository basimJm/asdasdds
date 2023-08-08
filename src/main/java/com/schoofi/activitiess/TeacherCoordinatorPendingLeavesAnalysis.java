package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
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
import com.schoofi.adapters.TeacherCoordinaterPendingLeavesAnalysisAdapter;
import com.schoofi.adapters.TeacherStudentBusAttendanceBusListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TeacherCoordinatorPendingLeavesAnalysis extends AppCompatActivity {

    private JSONArray teacherStudentBusListArray;
    private TeacherCoordinaterPendingLeavesAnalysisAdapter teacherStudentBusAttendanceBusListAdapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private ListView teacherStudentAttendanceListView;
    private TextView teacherPendingLeavesTotalCount;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_coordinator_pending_leaves_analysis);
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
            }
        });

        initData();
        getChairmanStudentLeaveList();

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getChairmanStudentLeaveList();
            }
        });



    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_PENDING_LEAVES_COUNT+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&cordinater_id="+Preferences.getInstance().userId);
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

            teacherStudentBusAttendanceBusListAdapter = new TeacherCoordinaterPendingLeavesAnalysisAdapter(TeacherCoordinatorPendingLeavesAnalysis.this, teacherStudentBusListArray);
            teacherStudentAttendanceListView.setAdapter(teacherStudentBusAttendanceBusListAdapter);
            teacherStudentBusAttendanceBusListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherCoordinatorPendingLeavesAnalysis.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_PENDING_LEAVES_COUNT;//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
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
                        Utils.showToast(TeacherCoordinatorPendingLeavesAnalysis.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(TeacherCoordinatorPendingLeavesAnalysis.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                       // JSONObject jsonObject = new JSONObject(responseObject.getString("sectionwise_pending_leaves"));
                        //JSONObject jsonObject = new JSONObject(String.valueOf(responseObject));

                        teacherPendingLeavesTotalCount.setText("Total Pending Leaves: "+responseObject.getJSONObject("responseObject").getString("pending_leaves"));

                        teacherStudentBusListArray= new JSONObject(String.valueOf(responseObject.getJSONObject("responseObject"))).getJSONArray("sectionwise_pending_leaves");


                        if(null!=teacherStudentBusListArray && teacherStudentBusListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentBusListArray.toString().getBytes();
                            VolleySingleton.getInstance(TeacherCoordinatorPendingLeavesAnalysis.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_PENDING_LEAVES_COUNT+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&cordinater_id="+Preferences.getInstance().userId,e);
                            teacherStudentAttendanceListView.invalidateViews();

                            teacherStudentBusAttendanceBusListAdapter = new TeacherCoordinaterPendingLeavesAnalysisAdapter(TeacherCoordinatorPendingLeavesAnalysis.this, teacherStudentBusListArray);
                            teacherStudentAttendanceListView.setAdapter(teacherStudentBusAttendanceBusListAdapter);
                            teacherStudentBusAttendanceBusListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(TeacherCoordinatorPendingLeavesAnalysis.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(TeacherCoordinatorPendingLeavesAnalysis.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(TeacherCoordinatorPendingLeavesAnalysis.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("session",Preferences.getInstance().session1);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("cordinater_id",Preferences.getInstance().userId);
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
