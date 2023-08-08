package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.AdminClassListAdapter;
import com.schoofi.adapters.AssesmentAllotedAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AssesmentAllotedScreen extends AppCompatActivity {

    private SwipyRefreshLayout swipyRefreshLayout;
    private JSONArray assesmentAllotedArray;
    private AssesmentAllotedAdapter assesmentAllotedAdapter;
    private ListView teacherStudentAttendanceListView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_assesment_student_subject_screen);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        teacherStudentAttendanceListView = (ListView) findViewById(R.id.listViewAdminBusListView);



        initData();
        getChairmanStudentLeaveList();

        teacherStudentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Intent intent = new Intent(AssesmentAllotedScreen.this,AssesmentNewQuestionsScreen.class);
                    intent.putExtra("test_id",assesmentAllotedArray.getJSONObject(position).getString("test_id"));
                    intent.putExtra("test_time",assesmentAllotedArray.getJSONObject(position).getString("test_maximum_time"));
                    intent.putExtra("value1","1");
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
        initData();
        getChairmanStudentLeaveList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                assesmentAllotedArray= null;
            }
            else
            {
                assesmentAllotedArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(assesmentAllotedArray!= null)
        {

            assesmentAllotedAdapter = new AssesmentAllotedAdapter(AssesmentAllotedScreen.this, assesmentAllotedArray);
            teacherStudentAttendanceListView.setAdapter(assesmentAllotedAdapter);
            assesmentAllotedAdapter.notifyDataSetChanged();
            swipyRefreshLayout.setRefreshing(false);
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AssesmentAllotedScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("po",response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(AssesmentAllotedScreen.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AssesmentAllotedScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("student_test_details"))
                    {
                        assesmentAllotedArray= new JSONObject(response).getJSONArray("student_test_details");


                        if(null!=assesmentAllotedArray && assesmentAllotedArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = assesmentAllotedArray.toString().getBytes();
                            VolleySingleton.getInstance(AssesmentAllotedScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId,e);
                            int count=0;
//                            for(int i=0;i<assesmentAllotedArray.length();i++)
//                            {
//                                count= count+Integer.parseInt(assesmentAllotedArray.getJSONObject(i).getString("total_stu"))  ;
//                            }

                            //totalClassStudents.setText("Total Students :"+String.valueOf(count));
                            teacherStudentAttendanceListView.invalidateViews();

                            assesmentAllotedAdapter = new AssesmentAllotedAdapter(AssesmentAllotedScreen.this, assesmentAllotedArray);
                            teacherStudentAttendanceListView.setAdapter(assesmentAllotedAdapter);
                            assesmentAllotedAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(AssesmentAllotedScreen.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AssesmentAllotedScreen.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(AssesmentAllotedScreen.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AssesmentAllotedScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AssesmentAllotedScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
