package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.TeacherStudentAttendanceAdapter;
import com.schoofi.adapters.TeacherStudentListSubjectAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TeacherSubjectStudentList extends AppCompatActivity {

    ImageView back;
    ListView studentListView;
    private JSONArray teacherStudentSubjectListArray;
    TeacherStudentListSubjectAdapter teacherStudentListSubjectAdapter;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    String value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Teacher Subject Student List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_teacher_subject_student_list);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        studentListView= (ListView) findViewById(R.id.listViewAddTask);
        value = getIntent().getStringExtra("value");

        if(value.matches("1")) {
            initData();
            getTeacherClassList();
        }

        else
        {
            initData1();
            getTeacherClassList1();
        }





        if(value.matches("1"))
        {

        }

        else {

            studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(TeacherSubjectStudentList.this, SchoolDiaryMainScreen.class);
                    intent.putExtra("position", position);


                    try {
                        Preferences.getInstance().loadPreference(getApplicationContext());
                        Preferences.getInstance().studentId = teacherStudentSubjectListArray.getJSONObject(position).getString("stu_id");
                        Preferences.getInstance().savePreference(getApplicationContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
        }

        mDrawableBuilder = TextDrawable.builder()
                .roundRect(10);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(value.matches("2"))
        {
            initData1();
            getTeacherClassList1();
        }
        else {
            initData();
            getTeacherClassList();
        }

    }


    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBJECT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2");
            if(e == null)
            {
                teacherStudentSubjectListArray= null;
            }
            else
            {
                teacherStudentSubjectListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentSubjectListArray!= null)
        {
            teacherStudentListSubjectAdapter= new TeacherStudentListSubjectAdapter(TeacherSubjectStudentList.this,teacherStudentSubjectListArray,"2");
            studentListView.setAdapter(teacherStudentListSubjectAdapter);
            teacherStudentListSubjectAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherSubjectStudentList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBJECT_LIST;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                 System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(TeacherSubjectStudentList.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(TeacherSubjectStudentList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Students"))

                    {

                        teacherStudentSubjectListArray= new JSONObject(response).getJSONArray("Students");
                        if(null!=teacherStudentSubjectListArray && teacherStudentSubjectListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentSubjectListArray.toString().getBytes();
                            VolleySingleton.getInstance(TeacherSubjectStudentList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBJECT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2",e);
                            studentListView.invalidateViews();
                            teacherStudentListSubjectAdapter= new TeacherStudentListSubjectAdapter(TeacherSubjectStudentList.this,teacherStudentSubjectListArray,"2");
                            studentListView.setAdapter(teacherStudentListSubjectAdapter);
                            teacherStudentListSubjectAdapter.notifyDataSetChanged();
                            // swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(TeacherSubjectStudentList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(TeacherSubjectStudentList.this, "Error fetching modules! Please try after sometime.");
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
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("value","2");
                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(TeacherSubjectStudentList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(TeacherSubjectStudentList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBJECT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                teacherStudentSubjectListArray= null;
            }
            else
            {
                teacherStudentSubjectListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentSubjectListArray!= null)
        {
            teacherStudentListSubjectAdapter= new TeacherStudentListSubjectAdapter(TeacherSubjectStudentList.this,teacherStudentSubjectListArray,"1");
            studentListView.setAdapter(teacherStudentListSubjectAdapter);
            teacherStudentListSubjectAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherSubjectStudentList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBJECT_LIST;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
              // System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(TeacherSubjectStudentList.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(TeacherSubjectStudentList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Students"))

                    {

                        teacherStudentSubjectListArray= new JSONObject(response).getJSONArray("Students");
                        if(null!=teacherStudentSubjectListArray && teacherStudentSubjectListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentSubjectListArray.toString().getBytes();
                            VolleySingleton.getInstance(TeacherSubjectStudentList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBJECT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            studentListView.invalidateViews();
                            teacherStudentListSubjectAdapter= new TeacherStudentListSubjectAdapter(TeacherSubjectStudentList.this,teacherStudentSubjectListArray,"1");
                            studentListView.setAdapter(teacherStudentListSubjectAdapter);
                            teacherStudentListSubjectAdapter.notifyDataSetChanged();
                           // swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(TeacherSubjectStudentList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(TeacherSubjectStudentList.this, "Error fetching modules! Please try after sometime.");
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
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("device_id", Preferences.getInstance().deviceId);
                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(TeacherSubjectStudentList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(TeacherSubjectStudentList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
