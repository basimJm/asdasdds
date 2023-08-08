package com.schoofi.activitiess;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.schoofi.adapters.ChairmanEmployeeAnalysisAdapter;
import com.schoofi.adapters.ChairmanEmployeeWithoutGraphListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CategoryStudentAnalysisVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;

public class ChairmanEmployeeWithoutGraphAnalysisScreen extends AppCompatActivity {

    private ImageView back,calendar,calendar1;
    String value;
    private MaterialSpinner departmentType;
    private String departmentId1="";
    ArrayList<String> departmentName;
    ArrayList<CategoryStudentAnalysisVO> departmentId;
    JSONObject jsonobject,jsonobject1,jsonObject2;
    JSONArray jsonarray,jsonarray1,employeeListArray;
    private ChairmanEmployeeWithoutGraphListAdapter chairmanEmployeeWithoutGraphListAdapter;
    private ListView employeeListView;
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String from1,to1;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    int selectedPosition = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chairman_employee_without_graph_analysis_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        value = getIntent().getStringExtra("value");

        calendar = (ImageView) findViewById(R.id.img_calendar_icon);

        employeeListView = (ListView) findViewById(R.id.listview_teacher_directory);
        departmentType = (MaterialSpinner) findViewById(R.id.spinner_category);
        departmentType.setBackgroundResource(R.drawable.grey_button);



        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(departmentId1.matches("null") || departmentId1.matches(""))
                {
                    Intent intent = new Intent(ChairmanEmployeeWithoutGraphAnalysisScreen.this,EmployeeAttendanceScreen.class);
                    intent.putExtra("value",4);
                    intent.putExtra("dept_id","");

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
                else
                {
                    Intent intent = new Intent(ChairmanEmployeeWithoutGraphAnalysisScreen.this,EmployeeAttendanceScreen.class);
                    intent.putExtra("value",4);
                    intent.putExtra("dept_id",departmentId1);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }



            }


        });

        calendar1 = (ImageView) findViewById(R.id.img_calendar_icon1);
        calendar1.setVisibility(View.GONE);

        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChairmanEmployeeWithoutGraphAnalysisScreen.this,ChairmanEmployeeAttendanceEmployeeWise.class);
                intent.putExtra("startingDate",from1);
                intent.putExtra("endingDate",to1);
                intent.putExtra("value","2");
                intent.putExtra("dept_id",departmentId1);
                startActivity(intent);
            }
        });

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChairmanEmployeeWithoutGraphAnalysisScreen.this,ChairmanEmployeeAttendanceEmployeeWise.class);
                intent.putExtra("startingDate",from1);
                intent.putExtra("endingDate",to1);
                intent.putExtra("value","2");
                intent.putExtra("dept_id",departmentId1);
                startActivity(intent);

            }
        });

        if(value.matches("1")) {

            cal.add(Calendar.DATE, -7);
            Date todate1 = cal.getTime();
            from1 = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
            System.out.println(from1);
            cal1.add(Calendar.DATE, -1);
            Date todate2 = cal1.getTime();
            to1 = new SimpleDateFormat("yyyy-MM-dd").format(todate2);
            if(departmentId1.matches("") || departmentId1.matches("null"))
            {

            }
            else {
                //initData();
                getTeacherClassList();
            }
        }
        else
        {
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");
            departmentId1 = getIntent().getStringExtra("dept_id");


            if(departmentId1.matches("") || departmentId1.matches("null"))
            {

            }
            else {
                //initData();
                getTeacherClassList();
            }
        }

        new DownloadJSON1().execute();
    }

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Preferences.getInstance().loadPreference(getApplicationContext());


            // Create an array to populate the spinner
            departmentId = new ArrayList<CategoryStudentAnalysisVO>();
            departmentName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
           // Log.d("URL", AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_DEPARTMENT_TYPE+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_DEPARTMENT_TYPE+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray1 = jsonobject1.getJSONArray("Departmentlist");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //ClassVO classVO = new ClassVO();
                    CategoryStudentAnalysisVO categoryStudentAnalysisVO = new CategoryStudentAnalysisVO();

                    categoryStudentAnalysisVO.setCategoryId(jsonobject1.optString("dept_id"));
                    departmentId.add(categoryStudentAnalysisVO);

                    departmentName.add(jsonobject1.optString("dept_name"));
                    if(departmentId1.matches("") || departmentId1.matches("null"))
                    {

                    }
                    else
                    {
                       for(int j = 0;j<jsonarray1.length();j++)
                       {
                           jsonObject2 = jsonarray1.getJSONObject(j);
                           if(jsonObject2.optString("dept_id").matches(departmentId1))
                           {
                               selectedPosition = j;

                           }
                           else
                           {

                           }

                       }
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            departmentType
                    .setAdapter(new ArrayAdapter<String>(ChairmanEmployeeWithoutGraphAnalysisScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,departmentName
                    ));

            if(departmentId1.matches("") || departmentId1.matches("null"))
            {

            }
            else {
                departmentType.setSelection(selectedPosition + 1);
            }

            departmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    departmentId1 = departmentId.get(position).getCategoryId().toString();

                    System.out.println(departmentId1);
                    //initData();
                    getTeacherClassList();












                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_WITHOUT_GRAPH_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+to1+"&from_dt="+from1+"&dept_id="+departmentId1+"&value="+value);
            if(e == null)
            {
                employeeListArray= null;
            }
            else
            {
                employeeListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(employeeListArray!= null)
        {
            chairmanEmployeeWithoutGraphListAdapter= new ChairmanEmployeeWithoutGraphListAdapter(ChairmanEmployeeWithoutGraphAnalysisScreen.this,employeeListArray);
            employeeListView.setAdapter(chairmanEmployeeWithoutGraphListAdapter);
            chairmanEmployeeWithoutGraphListAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_WITHOUT_GRAPH_DETAILS;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               // System.out.println(response);

                //Log.d("oiu",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_WITHOUT_GRAPH_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+to1+"&from_dt="+from1+"&dept_id="+departmentId1+"&value="+value);
                //System.out.println(url);
                Log.d("pp",response);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("employee_attendance"))

                    {

                        employeeListArray= new JSONObject(response).getJSONArray("employee_attendance");
                        if(null!=employeeListArray && employeeListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_WITHOUT_GRAPH_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&to_dt="+to1+"&from_dt="+from1+"&dept_id="+departmentId1+"&value="+value,e);
                            employeeListView.invalidateViews();
                            chairmanEmployeeWithoutGraphListAdapter= new ChairmanEmployeeWithoutGraphListAdapter(ChairmanEmployeeWithoutGraphAnalysisScreen.this,employeeListArray);
                            employeeListView.setAdapter(chairmanEmployeeWithoutGraphListAdapter);
                            chairmanEmployeeWithoutGraphListAdapter.notifyDataSetChanged();
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
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
                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("from_dt",from1);
                params.put("to_dt",to1);
                params.put("value",value);
                System.out.print(from1+to1);
                params.put("dept_id",departmentId1);
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
