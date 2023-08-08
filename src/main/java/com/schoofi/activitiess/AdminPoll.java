package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.schoofi.adapters.StudentPolllListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminPoll extends AppCompatActivity {
    private JSONArray studentPollArray;
    private ListView studentPollListView;
    StudentPolllListAdapter studentPolllListAdapter;
    String studentId = Preferences.getInstance().studentId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userType = Preferences.getInstance().userType;
    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admin Poll");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admin_poll);
        studentPollListView = (ListView) findViewById(R.id.listView_students_polls);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        studentPollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub


                try {
                    Intent intent = new Intent(AdminPoll.this,NewStudentSubmittedPollDetails.class);
                    intent.putExtra("position", position);
                    intent.putExtra("poll_id",studentPollArray.getJSONObject(position).getString("poll_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });

        initData();
        getStudentPollList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                studentPollArray= null;
            }
            else
            {
                studentPollArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentPollArray!= null)
        {
            studentPolllListAdapter= new StudentPolllListAdapter(AdminPoll.this,studentPollArray);
            studentPollListView.setAdapter(studentPolllListAdapter);
            studentPolllListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminPoll.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(AdminPoll.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminPoll.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("poll_List"))
                    {
                        studentPollArray= new JSONObject(response).getJSONArray("poll_List");
                        if(null!=studentPollArray && studentPollArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentPollArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminPoll.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
                            studentPollListView.invalidateViews();
                            studentPolllListAdapter = new StudentPolllListAdapter(AdminPoll.this, studentPollArray);
                            studentPollListView.setAdapter(studentPolllListAdapter);
                            studentPolllListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(AdminPoll.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminPoll.this, "Error fetching modules! Please try after sometime.");
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
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AdminPoll.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminPoll.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
