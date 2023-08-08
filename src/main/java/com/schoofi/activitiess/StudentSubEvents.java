package com.schoofi.activitiess;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.StudentEventListAdapter;
import com.schoofi.adapters.StudentEventViewPagerAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import astuetz.PagerSlidingTabStrip;

public class StudentSubEvents extends AppCompatActivity {
    private ListView studentEventListListView;
    private SwipyRefreshLayout swipyRefreshLayout;
    StudentEventListAdapter studentEventListAdapter;
    private JSONArray studentEventListArray;
    ImageView back,plus;
    TextView title;
    String value;
    String parentEventId;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student EventList");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_new_submitted_poll_list);



//        plus.setVisibility(View.INVISIBLE);
        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.txt_submittedPolls);
        title.setText("Events");
        value = getIntent().getStringExtra("value");
        parentEventId = getIntent().getStringExtra("event_id");



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final ProgressDialog loading = ProgressDialog.show(StudentEventList.this, "Loading new Data Please wait!!!", "",false,false);
//
//                final Thread thread = new Thread(){
//                    @Override
//                    public void run() {
//                        try {
//
//
//                            Thread.sleep(7000); // As I am using LENGTH_LONG in Toast
//                            loading.dismiss();
//
//                            StudentEventList.this.finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                //finish();
//
//                thread.start();
                finish();

            }
        });
        // Attach the view pager to the tab strip


        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        studentEventListListView = (ListView) findViewById(R.id.listview_student_submitted_polls);

        initData();
        getChairmanStudentLeaveList();


        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipyRefreshLayout.setRefreshing(true);
                initData();
                getChairmanStudentLeaveList();
            }
        });


        studentEventListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Preferences.getInstance().loadPreference(getApplicationContext());
                    if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {
                        Intent intent = new Intent(StudentSubEvents.this, StudentEventListDetails.class);
                        intent.putExtra("position", position);
                        intent.putExtra("value4", "5");
                        intent.putExtra("value", value);
                        intent.putExtra("parent_event", studentEventListArray.getJSONObject(position).getString("parent_event"));
                        intent.putExtra("program_name", studentEventListArray.getJSONObject(position).getString("program_name"));
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(StudentSubEvents.this, TeacherEventListDetails.class);
                        intent.putExtra("position", position);
                        intent.putExtra("value4", "5");
                        intent.putExtra("value", value);
                        intent.putExtra("parent_event", studentEventListArray.getJSONObject(position).getString("parent_event"));
                        intent.putExtra("program_name", studentEventListArray.getJSONObject(position).getString("program_name"));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*initData();
        getChairmanStudentLeaveList();*/
    }

    private void initData()
    {
Preferences.getInstance().loadPreference(getApplicationContext());

        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SUB_EVENT_LIST+"?event_id="+parentEventId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                studentEventListArray= null;
            }
            else
            {
                studentEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentEventListArray!= null)
        {
            studentEventListAdapter= new StudentEventListAdapter(StudentSubEvents.this,studentEventListArray,"2");
            studentEventListListView.setAdapter(studentEventListAdapter);
            studentEventListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        Log.d("event_id",parentEventId);
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentSubEvents.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SUB_EVENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                Log.d("op1",response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(StudentSubEvents.this, "No Records Found");
                        studentEventListListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentSubEvents.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        studentEventListArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentEventListArray && studentEventListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentEventListArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentSubEvents.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SUB_EVENT_LIST+"?event_id="+parentEventId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId,e);
                            studentEventListListView.setVisibility(View.VISIBLE);
                            studentEventListListView.invalidateViews();
                            studentEventListAdapter= new StudentEventListAdapter(StudentSubEvents.this,studentEventListArray,"2");
                            studentEventListListView.setAdapter(studentEventListAdapter);
                            studentEventListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(StudentSubEvents.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentSubEvents.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(StudentSubEvents.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("event_id",parentEventId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentSubEvents.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentSubEvents.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        final ProgressDialog loading = ProgressDialog.show(StudentEventList.this, "Loading new Data Please wait!!!", "",false,false);
//        final Thread thread = new Thread(){
//            @Override
//            public void run() {
//                try {
//
//                    Thread.sleep(7000); // As I am using LENGTH_LONG in Toast
//                    loading.dismiss();
//                    StudentEventList.this.finish();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        thread.start();
        finish();

    }
}
