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

public class AdminEmployeeAtttendanceFirstScreen extends AppCompatActivity {

    private TextView totalEmployeesAttendanceTaken,totalEmployeesPresent,totalEmployeesAbsent,totalEmployeeLeaves,employeeViewAnalysis;
    private ImageView back;
    private JSONArray chairmanDashBoardArray,chairmanDashBoardArray1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_employee_atttendance_first_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        totalEmployeesAttendanceTaken = (TextView) findViewById(R.id.text_total_employee_attendance_taken);
        totalEmployeesAbsent = (TextView) findViewById(R.id.text_total_employee_absent);
        totalEmployeesPresent = (TextView) findViewById(R.id.text_total_employee_present);
        totalEmployeeLeaves = (TextView) findViewById(R.id.text_total_employee_leave);
        employeeViewAnalysis = (TextView) findViewById(R.id.text_total_employee_view_analysis);

        employeeViewAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEmployeeAtttendanceFirstScreen.this,ChairmanEmployeeDepartmentWiseCount.class);
                intent.putExtra("value","0");
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_URL+"?school_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1);
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

        //hhhh

        if(chairmanDashBoardArray!= null)
        {
            /*chairmanStudentLeaveAdapter= new ChairmanStudentLeaveAdapter(ChairmanStudentLeaves.this,chairmanDashBoardArray);
            chairmanStudentLeaveListView.setAdapter(chairmanStudentLeaveAdapter);
            chairmanStudentLeaveAdapter.notifyDataSetChanged();*/

            try {

                totalEmployeesAttendanceTaken.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_employee_attendance"));
                totalEmployeesPresent.setText("Present "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_present_attendance"));
                totalEmployeesAbsent.setText("Absent "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_absent_attendance"));
                totalEmployeeLeaves.setText("Leave "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_leave_attendance"));
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
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                            VolleySingleton.getInstance(AdminEmployeeAtttendanceFirstScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_URL+"?school_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1,e);

                            totalEmployeesAttendanceTaken.setText(chairmanDashBoardArray.getJSONObject(0).getString("total_employee_attendance"));
                            totalEmployeesPresent.setText("Present "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_present_attendance"));
                            totalEmployeesAbsent.setText("Absent "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_absent_attendance"));
                            totalEmployeeLeaves.setText("Leave "+chairmanDashBoardArray.getJSONObject(0).getString("total_employee_leave_attendance"));
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
                params.put("school_id",Preferences.getInstance().schoolId);
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
