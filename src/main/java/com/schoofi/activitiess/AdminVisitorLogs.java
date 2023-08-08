package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminVisitorLogs extends AppCompatActivity {
    private TextView totalAssignmentsUploaded,todayAssignment,weeklyAssignment,viewAnalysis2;
    private JSONArray chairmanDashBoardArray,chairmanDashBoardArray1;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_visitor_logs);
        totalAssignmentsUploaded = (TextView) findViewById(R.id.text_total_assignments_uploaded);
        todayAssignment = (TextView) findViewById(R.id.text_today);
        weeklyAssignment = (TextView) findViewById(R.id.text_total_weekly);
        viewAnalysis2 = (TextView) findViewById(R.id.text_total_view_analysis2);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        viewAnalysis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminVisitorLogs.this,AdminVisitorAnalysisFirstScreen.class);
                startActivity(intent);

            }
        });

        initData();
        getChairmanStudentLeaveList();
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_FIRST_SERVICE+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                chairmanDashBoardArray= null;
            }
            else
            {
                chairmanDashBoardArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanDashBoardArray!= null)
        {
            /*chairmanStudentLeaveAdapter= new ChairmanStudentLeaveAdapter(ChairmanStudentLeaves.this,chairmanDashBoardArray);
            chairmanStudentLeaveListView.setAdapter(chairmanStudentLeaveAdapter);
            chairmanStudentLeaveAdapter.notifyDataSetChanged();*/

            try {

                totalAssignmentsUploaded.setText("Total "+chairmanDashBoardArray.getJSONObject(0).getString("total_visit_count"));
                todayAssignment.setText("Today "+chairmanDashBoardArray.getJSONObject(0).getString("daily_visit_count"));
                weeklyAssignment.setText("Weekly "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_visit_count"));
                //studentTotalExams.setText(chairmanDashBoardArray.getJSONObject(0).getString("exam_list"));

                // chairmanDashBoardChildListViewVOs = new ArrayList<>();

               /* for(int i=0;i<chairmanDashBoardArray.length();i++)
                {
                    jsonObject = chairmanDashBoardArray.getJSONObject(i);
                    chairmanDashBoardArray1 = jsonObject.getJSONArray("examArray");
                }

                for(int j=0;j<chairmanDashBoardArray1.length();j++)
                {
                    jsonObject1 = chairmanDashBoardArray1.getJSONObject(j);
                    ChairmanDashBoardChildListViewVO chairmanDashBoardChildListViewVO = new ChairmanDashBoardChildListViewVO(jsonObject1.getString("examName"),jsonObject1.getString("totalStudent"),jsonObject1.getString("grade_A"),jsonObject1.getString("grade_B"),jsonObject1.getString("grade_C"),jsonObject1.getString("grade_D"),jsonObject1.getString("grade_E"));
                    chairmanDashBoardChildListViewVOs.add(chairmanDashBoardChildListViewVO);



                }

                chairmanDashboardListviewAdapter = new ChairmanDashboardListviewAdapter(getApplicationContext(),chairmanDashBoardChildListViewVOs,temparr);
                litViewResults.setAdapter(chairmanDashboardListviewAdapter);
                chairmanDashboardListviewAdapter.notifyDataSetChanged();*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_FIRST_SERVICE/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        chairmanDashBoardArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=chairmanDashBoardArray && chairmanDashBoardArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanDashBoardArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminVisitorLogs.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_FIRST_SERVICE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);


                            totalAssignmentsUploaded.setText("Total "+chairmanDashBoardArray.getJSONObject(0).getString("total_visit_count"));
                            todayAssignment.setText("Today "+chairmanDashBoardArray.getJSONObject(0).getString("daily_visit_count"));
                            weeklyAssignment.setText("Weekly "+chairmanDashBoardArray.getJSONObject(0).getString("weekly_visit_count"));
                            /*studentTotalExams.setText(chairmanDashBoardArray.getJSONObject(0).getString("exam_list"));

                            chairmanDashBoardChildListViewVOs = new ArrayList<>();

                            for(int i=0;i<chairmanDashBoardArray.length();i++)
                            {
                                jsonObject = chairmanDashBoardArray.getJSONObject(i);
                                chairmanDashBoardArray1 = jsonObject.getJSONArray("examArray");
                            }

                            for(int j=0;j<chairmanDashBoardArray1.length();j++)
                            {
                                jsonObject1 = chairmanDashBoardArray1.getJSONObject(j);
                                ChairmanDashBoardChildListViewVO chairmanDashBoardChildListViewVO = new ChairmanDashBoardChildListViewVO(jsonObject1.getString("examName"),jsonObject1.getString("totalStudent"),jsonObject1.getString("grade_A"),jsonObject1.getString("grade_B"),jsonObject1.getString("grade_C"),jsonObject1.getString("grade_D"),jsonObject1.getString("grade_E"));
                                chairmanDashBoardChildListViewVOs.add(chairmanDashBoardChildListViewVO);



                            }

                           Log.d("koi",jsonObject1.toString());

                            chairmanDashboardListviewAdapter = new ChairmanDashboardListviewAdapter(getApplicationContext(),chairmanDashBoardChildListViewVOs,temparr);
                            litViewResults.setAdapter(chairmanDashboardListviewAdapter);
                            chairmanDashboardListviewAdapter.notifyDataSetChanged();*/
                        }
                    }
                    else
                        // Utils.showToast(ChairmanDashboard.this, "Error Fetching Response");
                        setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(ChairmanDashboard.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("session",Preferences.getInstance().session1);
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
