package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.schoofi.adapters.ChairmanStudentLeaveAdapter;
import com.schoofi.adapters.StudentAttendanceViewPagerAdapter;
import com.schoofi.adapters.StudentEventListAdapter;
import com.schoofi.adapters.StudentEventViewPagerAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.fragments.StudentUpcomingEvent;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import astuetz.PagerSlidingTabStrip;

public class StudentEventList extends AppCompatActivity  {

    private ListView studentEventListListView;
    private SwipyRefreshLayout swipyRefreshLayout;
    StudentEventListAdapter studentEventListAdapter;
    private JSONArray studentEventListArray;
    ImageView back,plus;
    TextView title;


    


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
        setContentView(R.layout.activity_student_attendance);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new StudentEventViewPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        plus = (ImageView) findViewById(R.id.img_plus);

        plus.setVisibility(View.INVISIBLE);
        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.txt_class);
        title.setText("Events");


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
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        /*swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        studentEventListListView = (ListView) findViewById(R.id.listview_eventList);

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

                Intent intent = new Intent(StudentEventList.this,StudentEventListDetails.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*initData();
        getChairmanStudentLeaveList();*/
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId);
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
            studentEventListAdapter= new StudentEventListAdapter(StudentEventList.this,studentEventListArray,"2");
            studentEventListListView.setAdapter(studentEventListAdapter);
            studentEventListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentEventList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EVENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(StudentEventList.this, "No Records Found");
                        studentEventListListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentEventList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("events"))
                    {
                        studentEventListArray= new JSONObject(response).getJSONArray("events");
                        if(null!=studentEventListArray && studentEventListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentEventListArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentEventList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId,e);
                            studentEventListListView.setVisibility(View.VISIBLE);
                            studentEventListListView.invalidateViews();
                            studentEventListAdapter= new StudentEventListAdapter(StudentEventList.this,studentEventListArray,"2");
                            studentEventListListView.setAdapter(studentEventListAdapter);
                            studentEventListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(StudentEventList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentEventList.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(StudentEventList.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
               // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("cls_id", Preferences.getInstance().studentClassId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentEventList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentEventList.this, "Unable to fetch data, kindly enable internet settings!");
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


