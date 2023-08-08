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
import com.schoofi.adapters.AdminHealthCardCommonListViewAdapter;
import com.schoofi.adapters.AdminStudentListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminHealthCardCommonListViewActivity extends AppCompatActivity {

    ImageView back,plus;
    SwipyRefreshLayout swipyRefreshLayout;
    ListView adminHealthCardListView;
    String value;
    private JSONArray adminHealthCardCommonListViewArray;
    private AdminHealthCardCommonListViewAdapter adminHealthCardCommonListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_health_card_common_list_view);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        plus = (ImageView) findViewById(R.id.img_new);

        value = getIntent().getStringExtra("value");

        adminHealthCardListView = (ListView) findViewById(R.id.listViewAdminBusListView);

        adminHealthCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Intent intent = new Intent(AdminHealthCardCommonListViewActivity.this,AdminHealthCardCommonDetailsActivity.class);
                    intent.putExtra("id",adminHealthCardCommonListViewArray.getJSONObject(position).getString("id"));
                    intent.putExtra("value",value);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        initData();
        getChairmanStudentLeaveList();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.matches("1"))
                {
                    Intent intent = new Intent(AdminHealthCardCommonListViewActivity.this,AddVacinationDetails.class);
                    startActivity(intent);
                }

                else
                    if(value.matches("3"))
                {
                    Intent intent = new Intent(AdminHealthCardCommonListViewActivity.this,AddInsuranceDetails.class);
                    startActivity(intent);
                }

                else
                    {
                        Intent intent = new Intent(AdminHealthCardCommonListViewActivity.this,AddMedicalHistoryDetails.class);
                        startActivity(intent);
                    }
            }
        });
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_HEALTH_COMMON_LISTVIEW+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&value="+value);
            if(e == null)
            {
                adminHealthCardCommonListViewArray= null;
            }
            else
            {
                adminHealthCardCommonListViewArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminHealthCardCommonListViewArray!= null)
        {

            adminHealthCardCommonListViewAdapter = new AdminHealthCardCommonListViewAdapter(AdminHealthCardCommonListViewActivity.this, adminHealthCardCommonListViewArray,value);
            adminHealthCardListView.setAdapter(adminHealthCardCommonListViewAdapter);
            adminHealthCardCommonListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminHealthCardCommonListViewActivity.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_HEALTH_COMMON_LISTVIEW/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(AdminHealthCardCommonListViewActivity.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminHealthCardCommonListViewActivity.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        adminHealthCardCommonListViewArray= new JSONObject(response).getJSONArray("responseObject");


                        if(null!=adminHealthCardCommonListViewArray && adminHealthCardCommonListViewArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminHealthCardCommonListViewArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminHealthCardCommonListViewActivity.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_HEALTH_COMMON_LISTVIEW+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&value="+value,e);
                            adminHealthCardListView.invalidateViews();

                            adminHealthCardCommonListViewAdapter = new AdminHealthCardCommonListViewAdapter(AdminHealthCardCommonListViewActivity.this, adminHealthCardCommonListViewArray,value);
                            adminHealthCardListView.setAdapter(adminHealthCardCommonListViewAdapter);
                            adminHealthCardCommonListViewAdapter.notifyDataSetChanged();
                            //swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(AdminHealthCardCommonListViewActivity.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminHealthCardCommonListViewActivity.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(AdminHealthCardCommonListViewActivity.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("value",value);
                //params.put("u_id",Preferences.getInstance().userId);

                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AdminHealthCardCommonListViewActivity.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminHealthCardCommonListViewActivity.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
