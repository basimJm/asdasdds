package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.ChairmanTaskAreaListViewAdapter;
import com.schoofi.adapters.HealthAndAuditInstitutionAdapter;
import com.schoofi.adapters.HealthAndAuditPersonAdapter;
import com.schoofi.adapters.HealthAndAuditTaskAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HealthAndAuditReviewForm extends AppCompatActivity {

    TextView institution, person, task;
    ListView healthAndAuditAuditorSubTaskList;
    TextView AreaTask;
    Button addReview;
    PopupWindow popupWindow3, popupWindow1, popupWindow2;
    HealthAndAuditPersonAdapter healthAndAuditPersonAdapter;
    HealthAndAuditTaskAdapter healthAndAuditTaskAdapter;
    HealthAndAuditInstitutionAdapter healthAndAuditInstitutionAdapter;
    ChairmanTaskAreaListViewAdapter chairmanTaskAreaListViewAdapter;
    private JSONArray healthAndAuditTaskArray, healthAndAuditPersonArray, healthAndAuditInstitutionArray, ChairmanTaskAreaArray;
    private ListView taskListView, personListView, institutionListView, taskAreaListView;
    int width;
    int height;
    String insId;
    String personId,roleId, taskId,personName,personPassword,personUserName;
    int j, count;
    String array1, array2;
    public ArrayList<String> attendance = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Health And Audit ReviewForm");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_health_and_audit_review_form);

        institution = (TextView) findViewById(R.id.spin_insitutionId);
        person = (TextView) findViewById(R.id.spin_personName);
        task = (TextView) findViewById(R.id.spin_task);
        taskAreaListView = (ListView) findViewById(R.id.listView_subtaskList);

        Preferences.getInstance().loadPreference(getApplicationContext());

        institution.setText(Preferences.getInstance().schoolName);
        insId = Preferences.getInstance().schoolId;

        healthAndAuditAuditorSubTaskList = (ListView) findViewById(R.id.listView_subtaskList);

        AreaTask = (TextView) findViewById(R.id.text_taskArea);
        AreaTask.setVisibility(View.GONE);
        addReview = (Button) findViewById(R.id.btn_add);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        //institutionListView = new ListView(getApplicationContext());

        taskAreaListView.setVisibility(View.GONE);

        //Utils.showToast(getApplicationContext(),Preferences.getInstance().schoolId);
        //Utils.showToast(getApplicationContext(),Preferences.getInstance().institutionId);

        taskAreaListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


       /* popupWindow1 = InstitutionListWindow();

        institution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                getTeacherStudentLeaveList();
                popupWindow1.showAsDropDown(view, -5, 0);
            }
        });*/


       /* taskAreaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Utils.showToast(getApplicationContext(),"hkk");
            }
        });*/



        /*institutionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                popupWindow1.dismiss();

                try {
                    institution.setText(healthAndAuditInstitutionArray.getJSONObject(position).getString("school_name"));
                    insId = healthAndAuditInstitutionArray.getJSONObject(position).getString("school_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

        personListView = new ListView(getApplicationContext());
        popupWindow2 = PersonListWindow();
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData1();
                getTeacherStudentLeaveList1();
                popupWindow2.showAsDropDown(view, -5, 0);
            }
        });

        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View v, int position, long id) {

                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                popupWindow2.dismiss();

                try {
                    person.setText(healthAndAuditPersonArray.getJSONObject(position).getString("name"));
                    personId = healthAndAuditPersonArray.getJSONObject(position).getString("user_id");
                    roleId = healthAndAuditPersonArray.getJSONObject(position).getString("role_id");
                    personUserName = healthAndAuditPersonArray.getJSONObject(position).getString("user_name");
                    personName = healthAndAuditPersonArray.getJSONObject(position).getString("name");
                    personPassword = healthAndAuditPersonArray.getJSONObject(position).getString("password");
                    //Utils.showToast(getApplicationContext(),roleId+personId+personName+personName+personPassword);
                    //Utils.showToast(getApplicationContext(),personId);
                    taskListView = new ListView(getApplicationContext());
                    popupWindow3 = TaskListWindow();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initData2();
                        getTeacherStudentLeaveList2();
                        popupWindow3.showAsDropDown(view, -5, 0);
                    }
                });

                taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                        fadeInAnimation.setDuration(10);
                        v.startAnimation(fadeInAnimation);

                        popupWindow3.dismiss();

                        try {
                            task.setText(healthAndAuditTaskArray.getJSONObject(position).getString("task"));
                            taskId = healthAndAuditTaskArray.getJSONObject(position).getString("task_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        AreaTask.setVisibility(View.VISIBLE);
                        taskAreaListView.setVisibility(View.VISIBLE);
                        getTeacherStudentLeaveList3();
                        taskAreaListView.setFocusable(true);


                    }
                });


            }
        });




        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k =0;k<j;k++)
                {
                    try {
                        if(chairmanTaskAreaListViewAdapter.ChairmanTaskAreaArray.getJSONObject(k).getString("isAdded").matches("1"))
                        {
                            attendance.add(chairmanTaskAreaListViewAdapter.ChairmanTaskAreaArray.getJSONObject(k).getString("id"));
                        }



                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                array1 = attendance.toString();
                array2 = array1.substring(1, array1.length()-1);
                System.out.println("ggg"+array2);
                postAttendance();
            }
        });


    }

    public PopupWindow InstitutionListWindow()
    {
        popupWindow1 = new PopupWindow(getApplicationContext());

        //initData2();
        //getChairmanClassList();
        initData();
        getTeacherStudentLeaveList();

        popupWindow1.setFocusable(true);
        popupWindow1.setWidth(width);
        popupWindow1.setHeight(200);


        popupWindow1.setContentView(institutionListView);

        return popupWindow1;
    }

    public PopupWindow TaskListWindow()
    {
        popupWindow3 = new PopupWindow(this);

        //initData2();
        //getChairmanClassList();
        initData2();
        getTeacherStudentLeaveList2();

        popupWindow3.setFocusable(true);
        popupWindow3.setWidth(width);
        popupWindow3.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        popupWindow3.setContentView(taskListView);

        return popupWindow3;
    }

    public PopupWindow PersonListWindow()
    {
        popupWindow2 = new PopupWindow(this);

        //initData2();
        //getChairmanClassList();
        initData1();
        getTeacherStudentLeaveList1();

        popupWindow2.setFocusable(true);
        popupWindow2.setWidth(width);
        popupWindow2.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        popupWindow2.setContentView(personListView);

        return popupWindow2;
    }

    private void initData()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_INSTITUTION_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                healthAndAuditInstitutionArray= null;
            }
            else
            {
                healthAndAuditInstitutionArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditInstitutionArray!= null)
        {
            healthAndAuditInstitutionAdapter= new HealthAndAuditInstitutionAdapter(HealthAndAuditReviewForm.this,healthAndAuditInstitutionArray);
            institutionListView.setAdapter(healthAndAuditInstitutionAdapter);
            healthAndAuditInstitutionAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherStudentLeaveList()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_INSTITUTION_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                       // mChart.setVisibility(View.INVISIBLE);
                        Utils.showToast(HealthAndAuditReviewForm.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditReviewForm.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("school_list"))
                    {
                        //mChart.setVisibility(View.VISIBLE);
                        healthAndAuditInstitutionArray= new JSONObject(response).getJSONArray("school_list");
                        if(null!=healthAndAuditInstitutionArray && healthAndAuditInstitutionArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = healthAndAuditInstitutionArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_INSTITUTION_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            institutionListView.invalidateViews();
                            healthAndAuditInstitutionAdapter= new HealthAndAuditInstitutionAdapter(HealthAndAuditReviewForm.this,healthAndAuditInstitutionArray);
                            institutionListView.setAdapter(healthAndAuditInstitutionAdapter);
                            healthAndAuditInstitutionAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Preferences.getInstance().loadPreference(HealthAndAuditReviewForm.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
               // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
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

    private void initData2()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_TASK_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                healthAndAuditTaskArray= null;
            }
            else
            {
                healthAndAuditTaskArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditTaskArray!= null)
        {
            healthAndAuditTaskAdapter= new HealthAndAuditTaskAdapter(HealthAndAuditReviewForm.this,healthAndAuditTaskArray);
            taskListView.setAdapter(healthAndAuditTaskAdapter);
            healthAndAuditTaskAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherStudentLeaveList2()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_TASK_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               // System.out.println(response);
               // System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        // mChart.setVisibility(View.INVISIBLE);
                        Utils.showToast(HealthAndAuditReviewForm.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditReviewForm.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("task"))
                    {
                        //mChart.setVisibility(View.VISIBLE);
                        healthAndAuditTaskArray= new JSONObject(response).getJSONArray("task");
                        if(null!=healthAndAuditTaskArray && healthAndAuditTaskArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = healthAndAuditTaskArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_TASK_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            taskListView.invalidateViews();
                            healthAndAuditTaskAdapter= new HealthAndAuditTaskAdapter(HealthAndAuditReviewForm.this,healthAndAuditTaskArray);
                            taskListView.setAdapter(healthAndAuditTaskAdapter);
                            healthAndAuditTaskAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Preferences.getInstance().loadPreference(HealthAndAuditReviewForm.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
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

    private void initData1()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_PERSON_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().schoolId);
            if(e == null)
            {
                healthAndAuditPersonArray= null;
            }
            else
            {
                healthAndAuditPersonArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditPersonArray!= null)
        {
            healthAndAuditPersonAdapter= new HealthAndAuditPersonAdapter(HealthAndAuditReviewForm.this,healthAndAuditPersonArray);
            personListView.setAdapter(healthAndAuditPersonAdapter);
            healthAndAuditPersonAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherStudentLeaveList1()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_PERSON_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               // System.out.println(response);
               // System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        // mChart.setVisibility(View.INVISIBLE);
                        Utils.showToast(HealthAndAuditReviewForm.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditReviewForm.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("users"))
                    {
                        //mChart.setVisibility(View.VISIBLE);
                        healthAndAuditPersonArray= new JSONObject(response).getJSONArray("users");
                        if(null!=healthAndAuditPersonArray && healthAndAuditPersonArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = healthAndAuditPersonArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_PERSON_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().schoolId,e);
                            personListView.invalidateViews();
                            healthAndAuditPersonAdapter= new HealthAndAuditPersonAdapter(HealthAndAuditReviewForm.this,healthAndAuditPersonArray);
                            personListView.setAdapter(healthAndAuditPersonAdapter);
                            healthAndAuditPersonAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Preferences.getInstance().loadPreference(HealthAndAuditReviewForm.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
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



    protected void getTeacherStudentLeaveList3()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_SUBTASK_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        // mChart.setVisibility(View.INVISIBLE);
                        Utils.showToast(HealthAndAuditReviewForm.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditReviewForm.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("sub_task"))
                    {
                        //mChart.setVisibility(View.VISIBLE);
                        ChairmanTaskAreaArray= new JSONObject(response).getJSONArray("sub_task");
                        for(j = 0;j<ChairmanTaskAreaArray.length();j++)
                        {
                            ChairmanTaskAreaArray.getJSONObject(j).put("isAdded","0");
                        }

                        count = j;
                        if(null!=ChairmanTaskAreaArray && ChairmanTaskAreaArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = ChairmanTaskAreaArray.toString().getBytes();
                            VolleySingleton.getInstance(HealthAndAuditReviewForm.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_SUBTASK_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&task_id="+taskId,e);
                            taskAreaListView.invalidateViews();
                            chairmanTaskAreaListViewAdapter= new ChairmanTaskAreaListViewAdapter(HealthAndAuditReviewForm.this,ChairmanTaskAreaArray);
                            taskAreaListView.setAdapter(chairmanTaskAreaListViewAdapter);
                            chairmanTaskAreaListViewAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Preferences.getInstance().loadPreference(HealthAndAuditReviewForm.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("task_id",taskId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
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

    protected void postAttendance()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_POST_AUDIT_REVIEW/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(HealthAndAuditReviewForm.this,"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(HealthAndAuditReviewForm.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(HealthAndAuditReviewForm.this,"Successfuly Created Audit");
                        finish();
                       // getTeacherStudentList2();
                        //submit.setEnabled(false);
                        //submit.setBackgroundResource(R.drawable.button_red_50_percent);
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(HealthAndAuditReviewForm.this, "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(HealthAndAuditReviewForm.this, "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(HealthAndAuditReviewForm.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());
                params.put("area_id", array2);
                //params.put("teach_email",Preferences.getInstance().userEmailId);
                //params.put("count", String.valueOf(i));
                params.put("school_id", insId);
                params.put("person_id", personId);
                params.put("task_id", taskId);
                params.put("role_id",roleId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("user_name",personUserName);
                params.put("name",personName);
                params.put("password",personPassword);
                //params.put("crr_date",date);
                //params.put("Students", "harsh");
                //params.put("u_email_id", Preferences.getInstance().userEmailId);

                Log.d("testing",array2+" "+insId+" "+personId+" "+taskId+" "+roleId+" "+Preferences.getInstance().institutionId+" "+Preferences.getInstance().token+" "+Preferences.getInstance().deviceId+" "+Preferences.getInstance().userId+" "+personUserName+" "+personName+" "+personPassword);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
        }



    }

}
