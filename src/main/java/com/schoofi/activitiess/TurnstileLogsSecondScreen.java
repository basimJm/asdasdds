package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.HealthAndAuditUserTaskListAdapter;
import com.schoofi.adapters.TurnstileDetailsLogAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import polypicker.model.Image;

public class TurnstileLogsSecondScreen extends AppCompatActivity {

    private ImageView back,unauthorisedAccess;
    private ListView turnstileDetailsLogsListView;
    private TurnstileDetailsLogAdapter turnstileDetailsLogAdapter;
    private JSONArray turnstileDetailsArray;
    private SwipyRefreshLayout swipyRefreshLayout;
    private TextView newView;
    String zoneName,zoneId,startDate,endDate,categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_turnstile_logs_second_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        unauthorisedAccess = (ImageView) findViewById(R.id.img_unauthorised);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);



        //zoneName = getIntent().getStringExtra("zone_name");
        zoneId = getIntent().getStringExtra("zone_id");
        startDate = getIntent().getStringExtra("start_date");
        endDate = getIntent().getStringExtra("end_date");
        categoryName = getIntent().getStringExtra("category");

        unauthorisedAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnstileLogsSecondScreen.this,UnauthorisedAccessUserScreen.class);

                intent.putExtra("category", categoryName);
                intent.putExtra("zone_id", zoneId);
                intent.putExtra("start_date", startDate);
                intent.putExtra("end_date", endDate);
                startActivity(intent);
                //startActivity(intent);
            }
        });

        turnstileDetailsLogsListView = (ListView) findViewById(R.id.listView_student_daily_attendance);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getClassList();
            }
        });
        newView = (TextView) findViewById(R.id.newView1);

        initData();
        getClassList();


    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TURNSTILE_LOG+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&zone_id="+zoneId+"&card_issued_type="+categoryName+"&from_date="+startDate+"&to_date="+endDate+"&permission="+"YES");
            if(e == null)
            {
                turnstileDetailsArray= null;
            }
            else
            {
                turnstileDetailsArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(turnstileDetailsArray!= null)
        {
            newView.setVisibility(View.GONE);
            turnstileDetailsLogsListView.invalidateViews();
            turnstileDetailsLogAdapter= new TurnstileDetailsLogAdapter(TurnstileLogsSecondScreen.this,turnstileDetailsArray,1);
            turnstileDetailsLogsListView.setAdapter(turnstileDetailsLogAdapter);
            turnstileDetailsLogAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TURNSTILE_LOG+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&zone_id="+zoneId+"&card_issued_type="+categoryName+"&from_date="+startDate+"&to_date="+endDate+"&permission="+"YES";
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                   // toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        //Utils.showToast(getApplicationContext(), "No Records Found");
                        newView.setVisibility(View.VISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        newView.setVisibility(View.VISIBLE);
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        turnstileDetailsArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=turnstileDetailsArray && turnstileDetailsArray.length()>=0)
                        {
                            newView.setVisibility(View.GONE);
                            Cache.Entry e = new Cache.Entry();
                            e.data = turnstileDetailsArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TURNSTILE_LOG+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&zone_id="+zoneId+"&card_issued_type="+categoryName+"&from_date="+startDate+"&to_date="+endDate+"&permission="+"YES",e);
                            turnstileDetailsLogsListView.invalidateViews();
                            turnstileDetailsLogAdapter= new TurnstileDetailsLogAdapter(TurnstileLogsSecondScreen.this,turnstileDetailsArray,1);
                            turnstileDetailsLogsListView.setAdapter(turnstileDetailsLogAdapter);
                            turnstileDetailsLogAdapter.notifyDataSetChanged();

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
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
}
