package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.schoofi.adapters.UserBookHistoryAdapter;
import com.schoofi.adapters.UserBookHistoryAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLibraryHistory extends AppCompatActivity {

    private ImageView back;
    private ListView userBookHistoryListView;
    private UserBookHistoryAdapter userBookHistoryAdapter;
    private JSONArray userBookHistoryArray;


    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_user_library_history);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userBookHistoryListView = (ListView) findViewById(R.id.student_leave_list);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                initData();
                getChairmanStudentLeaveList();
            }
        });

        initData();
        getChairmanStudentLeaveList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getChairmanStudentLeaveList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.LIBRARY_BOOK_HISTORY+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&user_id="+Preferences.getInstance().userId);
            if(e == null)
            {
                userBookHistoryArray= null;
            }
            else
            {
                userBookHistoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(userBookHistoryArray!= null)
        {

            userBookHistoryAdapter = new UserBookHistoryAdapter(UserLibraryHistory.this, userBookHistoryArray);
            userBookHistoryListView.setAdapter(userBookHistoryAdapter);
            userBookHistoryAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(UserLibraryHistory.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.LIBRARY_BOOK_HISTORY/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("po",response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(UserLibraryHistory.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(UserLibraryHistory.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("issued_books"))
                    {
                        userBookHistoryArray= new JSONObject(response).getJSONArray("issued_books");


                        if(null!=userBookHistoryArray && userBookHistoryArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = userBookHistoryArray.toString().getBytes();
                            VolleySingleton.getInstance(UserLibraryHistory.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.LIBRARY_BOOK_HISTORY+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&user_id="+Preferences.getInstance().userId,e);



                            userBookHistoryListView.invalidateViews();

                            userBookHistoryAdapter = new UserBookHistoryAdapter(UserLibraryHistory.this, userBookHistoryArray);
                            userBookHistoryListView.setAdapter(userBookHistoryAdapter);
                            userBookHistoryAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(UserLibraryHistory.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(UserLibraryHistory.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(UserLibraryHistory.this);
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
                params.put("user_id",Preferences.getInstance().userId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(UserLibraryHistory.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(UserLibraryHistory.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
