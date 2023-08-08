package com.schoofi.activitiess;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.gson.JsonArray;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.AdminBusListViewAdapter;
import com.schoofi.adapters.ChairmanStudentLeaveAdapter;
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


public class AdminBusListScreen extends AppCompatActivity {

    private SwipyRefreshLayout swipyRefreshLayout;
    private ListView busListView;
    private JSONArray adminBusListArray;
    private Button done;
    public ArrayList attendance = new ArrayList();
    public ArrayList attendance1 = new ArrayList();
    AdminBusListViewAdapter adminBusListViewAdapter;
    String array1,array2;
    String array3,array4;
    int i=0;
    public boolean c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admin Bus List Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admin_bus_list_screen);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        busListView = (ListView) findViewById(R.id.listViewAdminBusListView);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getChairmanStudentLeaveList();

            }
        });



        initData();
        getChairmanStudentLeaveList();

        done = (Button) findViewById(R.id.btn_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attendance = new ArrayList<String>();

                for(int k =0;k<i;k++)
                {
                    try {
                        System.out.println("array" +adminBusListViewAdapter.adminBusListArray.toString());
                        if(adminBusListViewAdapter.adminBusListArray.getJSONObject(k).getString("maps").toString().matches("P")) {

                            attendance.add(adminBusListViewAdapter.adminBusListArray.getJSONObject(k).getString("bus_number"));
                            attendance1.add(adminBusListViewAdapter.adminBusListArray.getJSONObject(k).getString("route_no"));
                        }

                        else
                        {
                            Log.d("kkk","kkk");
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                array1 = attendance.toString();
                array2 = array1.substring(1, array1.length()-1);
                array3 = attendance1.toString();
                array4 = array3.substring(1, array3.length()-1);
                //Utils.showToast(getApplicationContext(),array2.toString());

                Intent intent = new Intent(AdminBusListScreen.this,AboutSchoolContactUs.class);
                intent.putExtra("array",array2);
                intent.putExtra("array1",array4);
                startActivity(intent);

            }
        });



    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                adminBusListArray= null;
            }
            else
            {
                adminBusListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminBusListArray!= null)
        {

            adminBusListViewAdapter = new AdminBusListViewAdapter(AdminBusListScreen.this, adminBusListArray);
            busListView.setAdapter(adminBusListViewAdapter);
            adminBusListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminBusListScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(AdminBusListScreen.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminBusListScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("VehicleList"))
                    {
                        adminBusListArray= new JSONObject(response).getJSONArray("VehicleList");

                        for(i=0;i<adminBusListArray.length();i++)
                        {
                            adminBusListArray.getJSONObject(i).put("maps", "A");
                        }
                        if(null!=adminBusListArray && adminBusListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminBusListArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminBusListScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
                            busListView.invalidateViews();

                            adminBusListViewAdapter = new AdminBusListViewAdapter(AdminBusListScreen.this, adminBusListArray);
                            busListView.setAdapter(adminBusListViewAdapter);
                            adminBusListViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(AdminBusListScreen.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminBusListScreen.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(AdminBusListScreen.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
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
        if(Utils.isNetworkAvailable(AdminBusListScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminBusListScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();;
        getChairmanStudentLeaveList();
    }
}



