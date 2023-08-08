package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.schoofi.adapters.TeacherSelfLeaveListAdapter;
import com.schoofi.adapters.TutorialListViewDesignAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VideoLectureListScreen extends AppCompatActivity {

    private ImageView back;
    private ListView lectureListView;
    private TutorialListViewDesignAdapter studentLeaveListAdapter;
    private JSONArray studentLeaveListArray;
    private String subject_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_video_lecture_list_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lectureListView = (ListView) findViewById(R.id.listViewAddTask);
       subject_id = getIntent().getStringExtra("subject_id");


        initData();
        getStudentLeaveList();



        Preferences.getInstance().loadPreference(getApplicationContext());


        lectureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View convertView, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                try {
                    if(Preferences.getInstance().userRoleId.matches("4"))
                    {
                        Intent intent = new Intent(VideoLectureListScreen.this, TeacherStudentProgressUpdate.class);
                        intent.putExtra("position", position);
                        intent.putExtra("url", studentLeaveListArray.getJSONObject(position).getString("file_path"));
                        intent.putExtra("tutorial_id", studentLeaveListArray.getJSONObject(position).getString("tutorial_id"));
                        intent.putExtra("subject_id",subject_id);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(VideoLectureListScreen.this, StudentVideoView.class);
                        intent.putExtra("position", position);
                        intent.putExtra("url", studentLeaveListArray.getJSONObject(position).getString("file_path"));
                        intent.putExtra("tutorial_id", studentLeaveListArray.getJSONObject(position).getString("tutorial_id"));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getStudentLeaveList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_leave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TUTORIAL_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&class_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&subject_id="+subject_id);
            if(e == null)
            {
                studentLeaveListArray= null;
            }
            else
            {
                studentLeaveListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentLeaveListArray!= null)
        {
            studentLeaveListAdapter= new TutorialListViewDesignAdapter(VideoLectureListScreen.this,studentLeaveListArray);
            lectureListView.setAdapter(studentLeaveListAdapter);
            studentLeaveListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TUTORIAL_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&class_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&subject_id="+subject_id/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        Log.d("po",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&emp_id="+Preferences.getInstance().employeeId);
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                Log.d("io",response);

                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Lectures Found");
                        lectureListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        lectureListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("subject_details"))
                    {
                        lectureListView.setVisibility(View.VISIBLE);
                        studentLeaveListArray= new JSONObject(response).getJSONArray("subject_details");
                        if(null!=studentLeaveListArray && studentLeaveListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentLeaveListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TUTORIAL_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&class_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&subject_id="+subject_id,e);
                            lectureListView.invalidateViews();
                            studentLeaveListAdapter= new TutorialListViewDesignAdapter(VideoLectureListScreen.this,studentLeaveListArray);
                            lectureListView.setAdapter(studentLeaveListAdapter);
                            studentLeaveListAdapter.notifyDataSetChanged();
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
           /* @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("emp_id",Preferences.getInstance().employeeId);
                Log.d("emp_id",Preferences.getInstance().employeeId);
                //params.put("crr_date",currentDate);
                return params;
            }*/};

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
