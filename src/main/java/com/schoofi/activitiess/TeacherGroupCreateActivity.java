package com.schoofi.activitiess;

import android.app.SearchManager;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.DiaryGroupTeacherListAdapter;
import com.schoofi.adapters.DiaryMultipleStudentSelectionScreenListViewAdapter;
import com.schoofi.adapters.TeacherDiaryStudentGroupListAdapter;
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

public class TeacherGroupCreateActivity extends AppCompatActivity {

    private ListView diaryMultipleSelectionListView;
    private Button submit;
    private JSONArray diaryMultipleStudentSelectionArray;
    private DiaryGroupTeacherListAdapter diaryMultipleStudentSelectionScreenListViewAdapter;
    String array1,array2;
    public ArrayList<String> attendance = new ArrayList<String>();
    String typeId,ratingId,ratingBar,subjectId;
    private CheckBox checkBox;
    private String indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Multiple Student Selection Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_teacher_group_create);

        checkBox = (CheckBox) findViewById(R.id.checkbox);


        submit = (Button) findViewById(R.id.btn_submitAttendanceList);
        diaryMultipleSelectionListView = (ListView) findViewById(R.id.listViewTeacherStudentAttendanceDetails);

        initData();
        getChairmanStudentLeaveList();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int k=0;k<diaryMultipleStudentSelectionScreenListViewAdapter.teacherStudentAttendanceArray1.length();k++)
                {
                    try {
                        if(diaryMultipleStudentSelectionScreenListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isAdded").matches("1"))
                        {
                            attendance.add(diaryMultipleStudentSelectionScreenListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("user_id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Preferences.getInstance().loadPreference(getApplicationContext());

                attendance.add(Preferences.getInstance().studentId);

                array1 = attendance.toString();
                array2 = array1.substring(1, array1.length()-1);
                finish();
                //push from top to bottom
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

            }
        });

        if(checkBox.isChecked())
        {
            indicator = "true";
        }

        else
        {
            indicator = "false";
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBox.isChecked())
                {
                    indicator = "true";
                    for (int j = 0; j < diaryMultipleStudentSelectionArray.length(); j++) {
                        try {
                            diaryMultipleStudentSelectionArray.getJSONObject(j).put("isAdded", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)

                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = diaryMultipleStudentSelectionArray.toString().getBytes();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_TEACHER_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id",e);
                    diaryMultipleSelectionListView.invalidateViews();

                    diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryGroupTeacherListAdapter(TeacherGroupCreateActivity.this, diaryMultipleStudentSelectionArray);
                    diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
                    diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();
                }

                else {
                    indicator = "false";
                    for (int j = 0; j < diaryMultipleStudentSelectionArray.length(); j++) {
                        try {
                            diaryMultipleStudentSelectionArray.getJSONObject(j).put("isAdded", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)

                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = diaryMultipleStudentSelectionArray.toString().getBytes();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_TEACHER_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id",e);
                    diaryMultipleSelectionListView.invalidateViews();

                    diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryGroupTeacherListAdapter(TeacherGroupCreateActivity.this, diaryMultipleStudentSelectionArray);
                    diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
                    diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_TEACHER_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id");
            if(e == null)
            {
                diaryMultipleStudentSelectionArray= null;
            }
            else
            {
                diaryMultipleStudentSelectionArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(diaryMultipleStudentSelectionArray!= null)
        {

            diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryGroupTeacherListAdapter(TeacherGroupCreateActivity.this, diaryMultipleStudentSelectionArray);
            diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
            diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();
        }
    }



    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherGroupCreateActivity.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_TEACHER_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        diaryMultipleStudentSelectionArray= new JSONObject(response).getJSONArray("responseObject");

                        for(int i=0;i<diaryMultipleStudentSelectionArray.length();i++)
                        {
                            diaryMultipleStudentSelectionArray.getJSONObject(i).put("isAdded","0");
                        }


                        if(null!=diaryMultipleStudentSelectionArray && diaryMultipleStudentSelectionArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = diaryMultipleSelectionListView.toString().getBytes();
                            VolleySingleton.getInstance(TeacherGroupCreateActivity.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_TEACHER_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id",e);
                            diaryMultipleSelectionListView.invalidateViews();

                            diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryGroupTeacherListAdapter(TeacherGroupCreateActivity.this, diaryMultipleStudentSelectionArray);
                            diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
                            diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();



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
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);


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
