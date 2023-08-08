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

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activities.StudentAttendance;
import com.schoofi.adapters.NotificationIntentAdapter;
import com.schoofi.adapters.StudentEventListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.fragments.StudentAssignment;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationIntentClass extends AppCompatActivity {

    ImageView back;
    SwipyRefreshLayout swipyRefreshLayout;
    private ListView notificationListView;
    private NotificationIntentAdapter notificationIntentAdapter;
    private JSONArray notificationIntentAdapterArray;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_notification_intent_class);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        notificationListView = (ListView) findViewById(R.id.listView_notificationsList);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getChairmanStudentLeaveList();

            }
        });

        initData();
        getChairmanStudentLeaveList();

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Preferences.getInstance().loadPreference(getApplicationContext());

                try {

                if(Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8"))
                {

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches("50"))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this,ChairmanFeesAnalysis.class);
                            intent.putExtra("value","2");
                            startActivity(intent);
                            finish();
                        }

                        else
                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ATTENDANCE_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this,ChairmanSchoolAttendance.class);
                                startActivity(intent);
                                finish();
                            }

                            else
                                if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_LEAVE_TAG))
                                {
                                    Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentLeaves.class);
                                    startActivity(intent);
                                    finish();
                                }

                                else
                                if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ASSIGNMENT_TAG))
                                {

                                }

                                else
                                if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ANNOUNCEMENT_TAG))
                                {
                                    Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentAnnouncement.class);
                                    startActivity(intent);
                                    finish();
                                }

                                 else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEEDBACK_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentFeedBack.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EXAM_SCHEDULE_TAG))
                    {

                    }

                    else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_RESULT_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentResult.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EVENT_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentEvents.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_POLL_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentPoll.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_BUS_TRACKING_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this,ChairmanBusListScreen.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEES_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this,ChairmanStudentFees.class);
                        startActivity(intent);
                        finish();
                    }



                    if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.HEALTH_AND_SAFETY_ASSIGNER_TAG))
                    {
                        Intent intent = new Intent(NotificationIntentClass.this, HealthAndAuditHomeScreen.class);
                        startActivity(intent);
                        finish();
                    }



                    else
                    {

                    }






                }

                else
                    if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                    {
                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ATTENDANCE_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentAttendance.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_LEAVE_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentLeaveRequest.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ASSIGNMENT_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentAssignment.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ANNOUNCEMENT_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentAnnouncement.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEEDBACK_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentFeedBack.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EXAM_SCHEDULE_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentExamSchedule.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_RESULT_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentResult.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EVENT_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentEventList.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_POLL_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentPoll.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_BUS_TRACKING_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEES_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, ParentFees.class);
                            startActivity(intent);
                            finish();
                        }

                        if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.BUS_ATTENDANCE_TAG))
                        {
                            Intent intent = new Intent(NotificationIntentClass.this, ParentStudentBusAttendance.class);
                            startActivity(intent);
                            finish();
                        }

                        else
                        {

                        }
                    }

                    else
                        if(Preferences.getInstance().userRoleId.matches("4"))
                        {
                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ATTENDANCE_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherStudentAttendance.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_LEAVE_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherStudentLeave.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ASSIGNMENT_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherAssignment.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ANNOUNCEMENT_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherAnnouncement.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEEDBACK_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherStudentFeedback.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EXAM_SCHEDULE_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, StudentExamSchedule.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_RESULT_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherStudentResult.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EVENT_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, TeacherEventList.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_POLL_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, StudentPoll.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.BUS_ATTENDANCE_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, ParentStudentBusAttendance.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.HEALTH_AND_SAFETY_USER_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, AuditUserHomeScreen.class);
                                startActivity(intent);
                                finish();
                            }

                            if(notificationIntentAdapterArray.getJSONObject(position).getString("tag").matches(AppConstants.SERVER_URLS.HEALTH_AND_SAFETY_USER_TAG))
                            {
                                Intent intent = new Intent(NotificationIntentClass.this, AuditUserHomeScreen.class);
                                startActivity(intent);
                                finish();
                            }

                            else
                            {

                            }
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NOTIFICATION_LIST+"?user_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId);
            if(e == null)
            {
                notificationIntentAdapterArray= null;
            }
            else
            {
                notificationIntentAdapterArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(notificationIntentAdapterArray!= null)
        {
            notificationIntentAdapter= new NotificationIntentAdapter(NotificationIntentClass.this,notificationIntentAdapterArray);
            notificationListView.setAdapter(notificationIntentAdapter);
            notificationIntentAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(NotificationIntentClass.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NOTIFICATION_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(NotificationIntentClass.this, "No Records Found");
                        notificationListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(NotificationIntentClass.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("notifications"))
                    {
                        notificationIntentAdapterArray= new JSONObject(response).getJSONArray("notifications");
                        if(null!=notificationIntentAdapterArray && notificationIntentAdapterArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = notificationIntentAdapterArray.toString().getBytes();
                            VolleySingleton.getInstance(NotificationIntentClass.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NOTIFICATION_LIST+"?user_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId,e);
                            notificationListView.setVisibility(View.VISIBLE);
                            notificationListView.invalidateViews();

                            notificationIntentAdapter= new NotificationIntentAdapter(NotificationIntentClass.this,notificationIntentAdapterArray);
                            notificationListView.setAdapter(notificationIntentAdapter);
                            notificationIntentAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(NotificationIntentClass.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(NotificationIntentClass.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(NotificationIntentClass.this);
                Map<String,String> params = new HashMap<String, String>();
                /*;
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("user_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(NotificationIntentClass.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(NotificationIntentClass.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
